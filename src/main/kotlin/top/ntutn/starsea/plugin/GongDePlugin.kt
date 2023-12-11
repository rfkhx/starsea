package top.ntutn.starsea.plugin

import com.google.auto.service.AutoService
import top.ntutn.starseasdk.v2.BotContentProvider
import top.ntutn.starseasdk.v2.ITextChatContext

@AutoService(BotContentProvider::class)
class GongDePlugin: BotContentProvider {
    private var gongde = 0L

    override val pluginName: String
        get() = "gongde"

    override fun onTextMessage(context: ITextChatContext): Boolean {
        when (context.text) {
            "/start" -> context.replyWithText("你现在的功德：$gongde，快快攒功德 /gongde 吧！")
            "/gongde" -> {
                gongde++
                context.replyWithText("功德+1，现有 $gongde。 /gongde")
            }
            else -> return super.onTextMessage(context)
        }

        return true
    }
}