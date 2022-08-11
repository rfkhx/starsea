package top.ntutn.starsea.updateloop

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.ntutn.starsea.BotApi
import top.ntutn.starsea.util.BotScope
import top.ntutn.starsea.util.ConfigUtil
import top.ntutn.starseasdk.v1.TextChatContext

class TextChatContextImpl(override val chatId: String, override val text: String): TextChatContext {

    override fun replyWithText(text: String) {
        BotScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                BotApi.get().sendMessage(ConfigUtil.botToken, chatId, text)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }
}