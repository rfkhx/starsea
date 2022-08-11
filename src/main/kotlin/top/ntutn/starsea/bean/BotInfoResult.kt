package top.ntutn.starsea.bean

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BotInfoResult(
    val id: Long = -1L,
    @SerialName("is_bot")
    val isBot: Boolean = true,
    @SerialName("first_name")
    val firstName: String = "",
    @SerialName("username")
    val userName: String = "",
    @SerialName("can_join_groups")
    val canJoinGroups: Boolean = false,
    @SerialName("can_read_all_group_messages")
    val canReadAllGroupMessages: Boolean = false,
    @SerialName("supports_inline_queries")
    val supportsInlineQueries: Boolean = false,
)