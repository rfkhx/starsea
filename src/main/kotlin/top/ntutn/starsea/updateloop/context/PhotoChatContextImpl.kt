package top.ntutn.starsea.updateloop.context

import top.ntutn.starsea.updateloop.context.ChatContextImpl
import top.ntutn.starseasdk.v2.IPhotoChatContext
import top.ntutn.starseasdk.v2.IPhotoSize

class PhotoChatContextImpl(
    override val chatId: String,
    override val photoSizes: List<IPhotoSize>,
    override val text: String? = null
) : ChatContextImpl(chatId), IPhotoChatContext