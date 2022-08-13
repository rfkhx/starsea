package top.ntutn.starsea.plugin

import top.ntutn.starsea.util.LoggerOwner
import top.ntutn.starsea.util.slf4jLoggerOwner
import top.ntutn.starseasdk.v2.BotContentProvider

/**
 * 用于事件向插件的分发
 */
object PluginDispatcher: LoggerOwner by slf4jLoggerOwner<PluginManager>() {
    private val pluginList = mutableListOf<BotContentProvider>()
    private val commandRegistry = mutableMapOf<String, BotContentProvider>()

    fun init() {
        pluginList.addAll(PluginManager.getBotPlugins())
        pluginList.forEach { plugin ->
            plugin.registeredCommands.forEach {
                if (commandRegistry.containsKey(it)) {
                    logger.error("plugin {} and {} registered the same command /{}", plugin.pluginName, commandRegistry[it]!!.pluginName, it)
                    throw IllegalStateException("command conflict")
                }
                logger.info("registered command /{} for plugin {}", it, plugin.pluginName)
                commandRegistry[it] = plugin
            }
        }
    }

    /**
     * 普通的事件分发，类责任链模式
     */
    fun dispatch(handler: (BotContentProvider) -> Boolean): Boolean {
        pluginList.forEach {
            logger.trace("try to dispatch message to {}", it.pluginName)
            if (handler.invoke(it)) {
                logger.info("plugin {} handled the message", it.pluginName)
                return true
            }
        }
        return false
    }

    fun dispatchWithCommand(command: String, handler: (BotContentProvider) -> Boolean): Boolean {
        // 如果命令已经被注册
        val targetPlugin = commandRegistry[command]
        targetPlugin?.let {
            logger.info("message containing a registered command {}, try to dispatch it to {}", command, it.pluginName)
            if (handler.invoke(it)) {
                return true
            }
        }
        pluginList.forEach {
            if (it == targetPlugin) {
                return@forEach
            }
            logger.trace("try to dispatch message to {}", it.pluginName)
            if (handler.invoke(it)) {
                logger.info("plugin {} handled the message", it.pluginName)
                return true
            }
        }
        return false
    }
}
