package top.ntutn.starsea.updateloop.context

import com.google.auto.service.AutoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.ntutn.starsea.BotApi
import top.ntutn.starsea.util.BotScope
import top.ntutn.starsea.util.ConfigUtil
import top.ntutn.starseasdk.v2.IBotContext
import java.io.File

@AutoService(IBotContext::class)
class BotContextImpl: IBotContext {
    override fun sendCloudPhoto(chatId: String, photoFileId: String) = sendRemotePhoto(chatId, photoFileId)

    override fun sendLocalPhoto(chatId: String, photoFile: File) {
        BotScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                BotApi.get().sendPhoto(ConfigUtil.botToken, chatId, photoFile)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }


    override fun sendMessage(chatId: String, text: String) {
        BotScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                BotApi.get().sendMessage(ConfigUtil.botToken, chatId, text)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    override fun sendRemotePhoto(chatId: String, photoUrl: String) {
        BotScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                BotApi.get().sendPhoto(ConfigUtil.botToken, chatId, photoUrl)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }
}