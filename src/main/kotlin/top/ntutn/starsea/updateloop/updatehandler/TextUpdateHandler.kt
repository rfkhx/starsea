package top.ntutn.starsea.updateloop.updatehandler

import top.ntutn.starsea.bean.UpdateBean
import top.ntutn.starsea.plugin.PluginDispatcher
import top.ntutn.starsea.updateloop.context.TextChatContextImpl
import top.ntutn.starsea.util.LoggerOwner
import top.ntutn.starsea.util.slf4jLoggerOwner
import top.ntutn.starseasdk.v2.ITextChatContext

class TextUpdateHandler(override val nextHandler: AbstractUpdateHandler?) : AbstractUpdateHandler(),
    LoggerOwner by slf4jLoggerOwner<TextUpdateHandler>() {
    override fun dispatchUpdate(update: UpdateBean): Boolean {
        if (!update.message?.text.isNullOrBlank()) {
            val text = update.message?.text ?: return false
            logger.trace("识别到文本类型消息 {}", text)
            val command = text.takeIf { text.startsWith('/') }?.removePrefix("/")?.split(" ")?.firstOrNull()
            val context = TextChatContextImpl(update.message.chat.id.toString(), text)

            return if (command.isNullOrBlank()) {
                PluginDispatcher.dispatch { it.onTextMessage(context as ITextChatContext) }
            } else {
                PluginDispatcher.dispatchWithCommand(command) { it.onTextMessage(context as ITextChatContext) }
            }
        }
        return false
    }
}