package top.ntutn.starsea.bean

import kotlinx.serialization.Serializable

/**
 * This object represents a chat.
 * @param id Unique identifier for this chat. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
 * @param type Type of chat, can be either “private”, “group”, “supergroup” or “channel”
 */
@Serializable
data class ChatBean(
    val id: Long,
    val type: String,
)