package top.ntutn.starsea.bean

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserBean(
    val id: Long,
    @SerialName("is_bot") val isBot: Boolean,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String? = null,
    val username: String? = null
)