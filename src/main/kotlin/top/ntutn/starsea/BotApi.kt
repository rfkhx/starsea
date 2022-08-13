package top.ntutn.starsea

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import top.ntutn.starsea.bean.MessageBean
import top.ntutn.starsea.bean.ResultWrapperBean
import top.ntutn.starsea.bean.UpdateBean
import top.ntutn.starsea.bean.UserBean
import top.ntutn.starsea.network.RetrofitManager
import top.ntutn.starsea.network.Timeout
import top.ntutn.starsea.util.toMultiplePart
import top.ntutn.starsea.util.toRequestBody
import java.io.File
import java.util.concurrent.TimeUnit

@JvmInline
value class BotToken(val value: String)

/**
 * [Telegram Bot API](https://core.telegram.org/bots/api)
 */
interface BotApi {

    @GET("/bot{token}/getMe")
    suspend fun getMe(@Path("token") token: BotToken): ResultWrapperBean<UserBean>

    /**
     * 获取更新消息，长连接
     */
    @Headers(
        "${RetrofitManager.READ_TIMEOUT}: 6000",
        "${RetrofitManager.WRITE_TIMEOUT}: 6000",
        "${RetrofitManager.CONNECT_TIMEOUT}: 6000"
    )
    @Timeout(value = 60L, unit = TimeUnit.SECONDS)
    @GET("/bot{token}/getUpdates?timeout=30&allowed_updates=message,edited_message") // timeout单位秒
    suspend fun getUpdates(@Path("token") token: BotToken, @Query("offset") offset: Long? = null): ResultWrapperBean<List<UpdateBean>>

    /**
     * Use this method to send text messages.
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param text Text of the message to be sent, 1-4096 characters after entities parsing
     * @return On success, the sent [top.ntutn.starsea.bean.MessageBean] is returned.
     */
    @GET("/bot{token}/sendMessage") // timeout单位秒
    suspend fun sendMessage(@Path("token") token: BotToken, @Query("chat_id") chatId: String, @Query("text") text: String): ResultWrapperBean<MessageBean>

    /**
     * Use this method to send photos. On success, the sent Message is returned.
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param photo Photo to send. Pass a file_id as String to send a photo that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a photo from the Internet. The photo must be at most 10 MB in size. The photo's width and height must not exceed 10000 in total. Width and height ratio must be at most 20. More information on Sending Files »
     */
    @GET("/bot{token}/sendPhoto")
    suspend fun sendPhoto(@Path("token") token: BotToken, @Query("chat_id") chatId: String, @Query("photo") photo: String): ResultWrapperBean<MessageBean>

    /**
     * Use this method to send photos. On success, the sent Message is returned.
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param photo Photo to send. Pass a file_id as String to send a photo that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a photo from the Internet. The photo must be at most 10 MB in size. The photo's width and height must not exceed 10000 in total. Width and height ratio must be at most 20. More information on Sending Files »
     */
    @POST("/bot{token}/sendPhoto")
    @Multipart
    @Deprecated("仅用于retrofit")
    suspend fun sendPhoto(@Path("token") token: BotToken, @Part("chat_id") chatId: RequestBody, @Part photo: MultipartBody.Part): ResultWrapperBean<MessageBean>

    /**
     * 对上面方法的一个封装。
     */
    suspend fun sendPhoto(@Path("token") token: BotToken, @Part("chat_id") chatId: String, @Part photo: File): ResultWrapperBean<MessageBean>
        = sendPhoto(token, chatId.toRequestBody(), photo.toMultiplePart("photo"))

    companion object {
        fun get(): BotApi {
            return RetrofitManager.retrofit.create(BotApi::class.java)
        }

        @Deprecated("use get directly", replaceWith = ReplaceWith("get()"))
        fun getLongPollApi(): BotApi {
            return RetrofitManager.retrofit.create(BotApi::class.java)
        }
    }
}