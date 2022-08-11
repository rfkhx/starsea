package top.ntutn.starsea.util

import kotlinx.serialization.json.Json

object KotlinSerializeUtil {
    val json = Json { ignoreUnknownKeys = true }
}