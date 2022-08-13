package top.ntutn.starsea.plugin.adapter.v1

import top.ntutn.starseasdk.v1.TextChatContext
import top.ntutn.starseasdk.v2.ITextChatContext

class TextChatContextAdapter(private val adaptee: ITextChatContext): TextChatContext {
    override val chatId: String
        get() = adaptee.chatId
    override val text: String
        get() = adaptee.text
    override fun replyWithText(text: String) = adaptee.replyWithText(text)
}