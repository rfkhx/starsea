package top.ntutn.starsea.updateloop

import kotlinx.coroutines.*
import retrofit2.HttpException
import top.ntutn.starsea.BotApi
import top.ntutn.starsea.BotToken
import top.ntutn.starsea.arch.Handler
import top.ntutn.starsea.arch.Message
import top.ntutn.starsea.plugin.PluginManager
import top.ntutn.starsea.util.ApplicationContext
import top.ntutn.starsea.util.ConfigUtil
import top.ntutn.starsea.util.LoggerOwner
import top.ntutn.starsea.util.slf4jLoggerOwner
import top.ntutn.starseasdk.v1.BotContentProvider
import java.io.IOException

class FetchUpdateHandler : Handler(), LoggerOwner by slf4jLoggerOwner<FetchUpdateHandler>() {
    private lateinit var job: Job
    private var confirmed = 0L
    private val botApi by lazy {
        BotApi.get()
    }
    private val botContentPlugins by lazy {
        PluginManager.loadServices<BotContentProvider>()
    }
    var failedTimes = 0

    override fun handleMessage(message: Message) {
        super.handleMessage(message)
        job = SupervisorJob()

        if (ApplicationContext.exiting) {
            return
        }
        CoroutineScope(Dispatchers.IO + job).launch {
            kotlin.runCatching {
                singleFetch()
            }.onSuccess {
                failedTimes = 0
            }.onFailure {
                if (it is CancellationException) {
                    throw it
                }
                failedTimes++
                logger.error("获取消息失败，重试计数{}", failedTimes, it)
                if (it is HttpException) {
                    logger.debug(
                        "code = {}, message= {}, error = {}",
                        it.code(),
                        it.message(),
                        it.response()?.errorBody()
                    )
                }
                if (failedTimes >= 5) {
                    throw RuntimeException("too much network failure.")
                }
            }.getOrThrow()
            sendMessage(Message(0))
        }
    }

    fun startFetch() {
        sendMessage(Message(0))
    }

    /**
     * 一次获取消息的过程
     * @throws IOException 网络异常时抛出
     */
    @Throws(IOException::class, HttpException::class)
    suspend fun singleFetch() {
        logger.trace("尝试获取消息")
        val result = kotlin.runCatching {
            botApi.getUpdates(BotToken(ConfigUtil.configBean.botToken), confirmed + 1)
        }.onSuccess {
            logger.debug("取到消息{}", it)
            require(it.ok)
        }.getOrThrow().result
        withContext(Dispatchers.Main) {
            confirmed = result.maxOfOrNull { it.updateId } ?: return@withContext
            result.forEach { update ->
                (update.message ?: update.editedMessage)?.let {
                    when {
                        !it.text.isNullOrBlank() -> {
                            logger.info("received text message {} from {}", it.text, it.chat)
                            val context = TextChatContextImpl(it.chat.id.toString(), it.text)
                            var handled: Boolean
                            for (i in botContentPlugins) {
                                logger.debug("尝试交给{}处理", i.pluginName)
                                handled = i.onTextMessage(context)
                                if (handled) {
                                    logger.info("{} 处理了这条消息", i.pluginName)
                                    break
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun stopFetch() {
        job.cancel()
        clearMessages()
    }
}