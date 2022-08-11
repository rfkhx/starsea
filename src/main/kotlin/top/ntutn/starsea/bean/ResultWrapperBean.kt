package top.ntutn.starsea.bean

import kotlinx.serialization.Serializable

@Serializable
data class ResultWrapperBean<T>(val ok: Boolean = false, val result: T)
