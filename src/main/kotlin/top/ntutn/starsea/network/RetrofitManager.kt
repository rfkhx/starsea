package top.ntutn.starsea.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import top.ntutn.starsea.util.KotlinSerializeUtil
import java.util.concurrent.TimeUnit

object RetrofitManager {
    private const val BASE_URL = "https://api.telegram.org/"

    const val CONNECT_TIMEOUT = "CONNECT_TIMEOUT"
    const val READ_TIMEOUT = "READ_TIMEOUT"
    const val WRITE_TIMEOUT = "WRITE_TIMEOUT"

    const val DEFAULT_TIMEOUT = 15

    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(1L, TimeUnit.DAYS) // 超时相关逻辑由 [top.ntutn.starsea.network.TimeoutCallAdapterFactory] 控制
            .readTimeout(1L, TimeUnit.DAYS)
            .writeTimeout(1L, TimeUnit.DAYS)
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(KotlinSerializeUtil.json.asConverterFactory(MediaType.parse("application/json")!!))
            .addCallAdapterFactory(TimeoutCallAdapterFactory())
            .client(okHttpClient)
            .build()
    }
}