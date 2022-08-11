package top.ntutn.starsea.bean

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a message.
 * @param messageId Unique message identifier inside this chat
 * @param chat Conversation the message belongs to
 * @param text Optional. For text messages, the actual UTF-8 text of the message
 */
@Serializable
data class MessageBean(@SerialName("message_id") val messageId: Long, val chat: ChatBean, val text: String? = null, val photo: List<PhotoSizeBean>? = null)