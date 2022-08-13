package top.ntutn.starsea.plugin.adapter.v1

import top.ntutn.starsea.util.LoggerOwner
import top.ntutn.starsea.util.slf4jLoggerOwner
import top.ntutn.starseasdk.v2.ITextChatContext
import top.ntutn.starseasdk.v1.BotContentProvider as V1Adapter
import top.ntutn.starseasdk.v2.BotContentProvider as V2Provider

class BotContentProviderAdapter(private val adaptee: V1Adapter): V2Provider, LoggerOwner by slf4jLoggerOwner<BotContentProviderAdapter>() {
    init {
        logger.warn("{} 插件使用了旧版本API，现通过兼容方式装入，请联系开发者进行升级适配！", adaptee.pluginName)
    }

    override val pluginName: String = adaptee.pluginName
    override fun onPluginLoaded() {
        super.onPluginLoaded()
        adaptee.onPluginLoaded()
    }

    override fun onTextMessage(context: ITextChatContext): Boolean {
        return adaptee.onTextMessage(TextChatContextAdapter(context))
    }
}