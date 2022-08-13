package top.ntutn.starsea.updateloop

import kotlinx.coroutines.*
import retrofit2.HttpException
import top.ntutn.starsea.BotApi
import top.ntutn.starsea.BotToken
import top.ntutn.starsea.arch.ApplicationContext
import top.ntutn.starsea.arch.Handler
import top.ntutn.starsea.arch.Message
import top.ntutn.starsea.updateloop.updatehandler.PhotoUpdateHandler
import top.ntutn.starsea.updateloop.updatehandler.StubUpdateHandler
import top.ntutn.starsea.updateloop.updatehandler.TextUpdateHandler
import top.ntutn.starsea.util.ConfigUtil
import top.ntutn.starsea.util.LoggerOwner
import top.ntutn.starsea.util.slf4jLoggerOwner
import java.io.IOException

class FetchUpdateHandler : Handler(), LoggerOwner by slf4jLoggerOwner<FetchUpdateHandler>() {
    private lateinit var job: Job
    private var confirmed = 0L
    private val botApi by lazy {
        BotApi.get()
    }
    private val updateHandlers = PhotoUpdateHandler(TextUpdateHandler(StubUpdateHandler()))
    private var failedTimes = 0

    override fun handleMessage(message: Message) {
        super.handleMessage(message)
        job = SupervisorJob()

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
                if (failedTimes >= 5 || ApplicationContext.exiting) {
                    throw RuntimeException("too much network failure.", it)
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

        if (ApplicationContext.exiting) {
            // 之所以放到这里才抛出异常，是因为要多进行一次请求，确认停机前最后一条消息
            throw IllegalStateException("System is closing")
        }
        withContext(Dispatchers.Main) {
            confirmed = result.maxOfOrNull { it.updateId } ?: return@withContext
            result.forEach { update ->
                updateHandlers.handleUpdate(update)
            }
        }
    }

    fun stopFetch() {
        job.cancel()
        clearMessages()
    }
}