package top.ntutn.starsea.util

import kotlinx.coroutines.GlobalScope
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

fun String.toRequestBody(): RequestBody {
    return RequestBody.create(MediaType.parse("text/plain"), this)
}

fun File.toMultiplePart(parameter: String): MultipartBody.Part {
    return MultipartBody.Part.createFormData(parameter, name, RequestBody.create(MediaType.parse("multipart/form-data"), this))
}

val BotScope get() = GlobalScope