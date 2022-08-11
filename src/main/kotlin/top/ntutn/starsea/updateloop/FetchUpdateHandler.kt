package top.ntutn.starsea.updateloop

import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.HttpException
import top.ntutn.starsea.BotApi
import top.ntutn.starsea.BotToken
import top.ntutn.starsea.plugin.PluginManager
import top.ntutn.starsea.util.*
import top.ntutn.starseasdk.v1.BotContentProvider
import java.io.File
import java.io.IOException
import kotlin.math.log

class FetchUpdateHandler : LoggerOwner by slf4jLoggerOwner<FetchUpdateHandler>() {
    private lateinit var job: Job
    private var confirmed = 0L
    private val botApi by lazy {
        BotApi.get()
    }
    private val botContentPlugins by lazy {
        PluginManager.loadServices<BotContentProvider>()
    }

    fun startFetch() {
        job = SupervisorJob()
        CoroutineScope(Dispatchers.IO + job).launch {
            var failedTimes = 0
            while (true) {
                kotlin.runCatching {
                    singleFetch()
                }.onSuccess {
                    failedTimes = 0
                }.onFailure {
                    failedTimes++
                    logger.error("获取消息失败，重试计数{}", failedTimes, it)
                    if (it is HttpException) {
                        logger.debug("code = {}, message= {}, error = {}", it.code(), it.message(), it.response()?.errorBody())
                    }
                    if (failedTimes > 5) {
                        // TODO 直接退出整个机器人
                        stopFetch()
                    }
                }
            }
        }
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
    }
}