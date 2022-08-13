package top.ntutn.starsea.plugin

import top.ntutn.starsea.arch.Handler
import top.ntutn.starsea.arch.Looper
import top.ntutn.starsea.arch.Message
import top.ntutn.starsea.plugin.adapter.v1.BotContentProviderAdapter
import top.ntutn.starsea.util.LoggerOwner
import top.ntutn.starsea.util.slf4jLoggerOwner
import top.ntutn.starseasdk.v2.BotContentProvider
import java.io.File
import java.util.ServiceLoader

object PluginManager : LoggerOwner by slf4jLoggerOwner<PluginManager>() {
    private val pluginClassLoader = PluginClassLoader()
    private val registeredPlugins = mutableListOf<BotContentProvider>()
    private var loadedPlugins = false

    private val handler = object : Handler(Looper.mainLooper()) {
        override fun handleMessage(message: Message) {
            super.handleMessage(message)
            logger.trace("tick")
            registeredPlugins.forEach {
                it.tick()
            }
            sendEmptyMessageDelayed(60_000)
        }
    }

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
        loadServices().forEach {
            logger.info("loaded {}", it.pluginName)
            registeredPlugins.add(it)
            it.onPluginLoaded()
        }
        loadedPlugins = true
        handler.sendEmptyMessage()
    }

    private fun loadServices(): List<BotContentProvider> {
        val classes = loadBySPI<BotContentProvider>().toMutableList()
        classes.addAll(loadBySPI<top.ntutn.starseasdk.v1.BotContentProvider>().map { BotContentProviderAdapter(it) })
        return classes
    }

    private inline fun <reified T> loadBySPI() = ServiceLoader.load(T::class.java, pluginClassLoader)

    fun getBotPlugins(): Collection<BotContentProvider> = registeredPlugins
}