package top.ntutn.starsea.util

import kotlinx.serialization.decodeFromString
import top.ntutn.starsea.BotToken
import top.ntutn.starsea.bean.JsonConfigBean
import java.io.File

object ConfigUtil {
    val configBean by lazy {
        val configString = File("config.json").readText()
        return@lazy KotlinSerializeUtil.json.decodeFromString<JsonConfigBean>(configString)
    }

    val botToken by lazy {
        BotToken(configBean.botToken)
    }
}