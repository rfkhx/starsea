package top.ntutn.starsea

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers
import top.ntutn.starsea.arch.Looper
import top.ntutn.starsea.plugin.PluginClassLoader
import top.ntutn.starsea.plugin.PluginManager
import top.ntutn.starsea.updateloop.FetchUpdateHandler
import top.ntutn.starsea.util.ApplicationContext
import top.ntutn.starsea.util.ConfigUtil
import top.ntutn.starsea.util.Slf4jLoggerOwner
import top.ntutn.starsea.util.slf4jLogger
import kotlin.system.exitProcess

object StarSea

@OptIn(DelicateCoroutinesApi::class)
fun main(args: Array<String>) {
    val logger = slf4jLogger<StarSea>()

    logger.info("机器人框架正在启动")

    Looper.prepareMainLooper()

    ApplicationContext.init()
    PluginManager.loadPlugins()

    GlobalScope.launch(Dispatchers.Main) {
        val configBean = ConfigUtil.configBean

        val botInfo = kotlin.runCatching {
            withContext(Dispatchers.IO) {
                BotApi.get().getMe(BotToken(configBean.botToken))
            }
        }

        if (botInfo.isSuccess && botInfo.getOrNull()?.ok == true) {
            logger.info("获取机器人信息成功，botInfo={}", botInfo)
            FetchUpdateHandler().startFetch()
        } else {
            logger.error("获取机器人信息失败！botInfo={}", botInfo.getOrNull(), botInfo.exceptionOrNull())
            throw RuntimeException("获取机器人信息失败！")
        }
    }
    Looper.loop()
}
