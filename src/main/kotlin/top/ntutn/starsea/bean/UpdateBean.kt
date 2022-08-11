package top.ntutn.starsea.bean

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateBean(
    @SerialName("update_id") val updateId: Long,
    val message: MessageBean? = null,
    @SerialName("edited_message") val editedMessage: MessageBean? = null
)