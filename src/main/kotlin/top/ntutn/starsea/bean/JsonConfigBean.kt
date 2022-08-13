package top.ntutn.starsea.bean

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.ntutn.starsea.util.slf4jLogger
import java.util.UUID

@Serializable
data class JsonConfigBean(@SerialName("bot_token") val botToken: String, @SerialName("super_pwd") var superPwd: String? = null) {
    init {
        if (superPwd.isNullOrBlank()) {
            superPwd = UUID.randomUUID().toString()
            val logger = slf4jLogger<JsonConfigBean>()
            logger.warn("超级管理员密码未设置，为您指定为 {}", superPwd)
        }
    }
}
