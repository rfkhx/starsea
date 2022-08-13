package top.ntutn.starsea.updateloop.context

import top.ntutn.starseasdk.v2.ITextChatContext

class TextChatContextImpl(override val chatId: String, override val text: String) : ChatContextImpl(chatId),
    ITextChatContext {
    override val command: String? by lazy {
        text.takeIf { it.startsWith("/") }?.removePrefix("/")?.split(" ")?.firstOrNull()
    }
    override val params: List<String>? by lazy {
        text.takeIf { command != null }?.split(" ")?.mapIndexedNotNull { index, s -> s.takeIf { index > 0 } }
    }
}