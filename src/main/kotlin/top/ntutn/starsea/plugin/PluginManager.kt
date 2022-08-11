package top.ntutn.starsea.plugin

import top.ntutn.starsea.util.LoggerOwner
import top.ntutn.starsea.util.slf4jLoggerOwner
import top.ntutn.starseasdk.v1.BotContentProvider
import java.io.File
import java.util.ServiceLoader

object PluginManager: LoggerOwner by slf4jLoggerOwner<PluginManager>() {
    private val pluginClassLoader = PluginClassLoader()

    fun loadPlugins() {
        logger.info("loading plugins...")
        File("plugin")
            .takeIf { it.exists() && it.isDirectory }
            ?.listFiles()
            ?.filter { it.extension == "jar" }
            ?.forEach {
                logger.debug("发现插件{}, 加入插件系统", it)
                pluginClassLoader.addPlugin(it)
            }
        loadServices<BotContentProvider>().forEach {
            logger.info("loaded {}", it.pluginName)
            it.onPluginLoaded()
        }
    }

    fun <T> loadServices(clazz: Class<T>) = ServiceLoader.load(clazz, pluginClassLoader)

    inline fun <reified T> loadServices() = loadServices(T::class.java)
}