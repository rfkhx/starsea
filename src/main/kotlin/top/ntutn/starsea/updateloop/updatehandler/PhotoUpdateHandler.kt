package top.ntutn.starsea.updateloop.updatehandler

import top.ntutn.starsea.bean.UpdateBean
import top.ntutn.starsea.plugin.PluginDispatcher
import top.ntutn.starsea.updateloop.context.PhotoChatContextImpl
import top.ntutn.starsea.util.LoggerOwner
import top.ntutn.starsea.util.slf4jLoggerOwner

class PhotoUpdateHandler(override val nextHandler: AbstractUpdateHandler?): AbstractUpdateHandler(), LoggerOwner by slf4jLoggerOwner<PhotoUpdateHandler>() {
    override fun dispatchUpdate(update: UpdateBean): Boolean {
        if (update.message?.photo != null) {
            logger.trace("识别到photo类型消息")
            val context = PhotoChatContextImpl(update.message.chat.id.toString(), update.message.photo, update.message.text)
            PluginDispatcher.dispatch { it.onPhotoMessage(context) }
        }
        return false
    }
}