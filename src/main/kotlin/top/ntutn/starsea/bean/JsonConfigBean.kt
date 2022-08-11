package top.ntutn.starsea.bean

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JsonConfigBean(@SerialName("bot_token") val botToken: String = "")
