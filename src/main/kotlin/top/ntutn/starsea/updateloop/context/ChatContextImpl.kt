package top.ntutn.starsea.updateloop.context

import top.ntutn.starseasdk.v2.BotContext
import top.ntutn.starseasdk.v2.IBotContext
import top.ntutn.starseasdk.v2.IChatContext
import java.io.File

open class ChatContextImpl(override val chatId: String) : IChatContext, IBotContext by BotContext {

    override fun replyWithCloudPhoto(photoFileId: String) = sendCloudPhoto(chatId, photoFileId)

    override fun replyWithLocalPhoto(photoFile: File) = sendLocalPhoto(chatId, photoFile)

    override fun replyWithRemotePhoto(photoUrl: String) = sendRemotePhoto(chatId, photoUrl)

    override fun replyWithText(text: String) = sendMessage(chatId, text)
}