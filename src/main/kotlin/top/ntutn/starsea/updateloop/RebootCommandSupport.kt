package top.ntutn.starsea.updateloop

import com.google.auto.service.AutoService
import top.ntutn.starsea.util.ApplicationContext
import top.ntutn.starseasdk.v1.BotContentProvider
import top.ntutn.starseasdk.v1.TextChatContext

@AutoService(BotContentProvider::class)
class RebootCommandSupport: BotContentProvider {
    override val pluginName: String
        get() = "reboot command"

    override fun onTextMessage(context: TextChatContext): Boolean {
        if (context.text == "/power") {
            ApplicationContext.shutdown()
            return true
        }
        return super.onTextMessage(context)
    }
}