package top.ntutn.starsea.bean

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents one size of a photo or a file / sticker thumbnail.
 * @param fileId Identifier for this file, which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param width Photo width
 * @param height Photo height
 * @param fileSize Optional. File size in bytes
 */
@Serializable
data class PhotoSizeBean(
    @SerialName("file_id") val fileId: String,
    @SerialName("file_unique_id") val fileUniqueId: String,
    val width: Int,
    val height: Int,
    @SerialName("file_size") val fileSize: Int? = null
)
