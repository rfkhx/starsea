package top.ntutn.starsea.network

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import top.ntutn.starsea.arch.Looper
import top.ntutn.starsea.util.LoggerOwner
import top.ntutn.starsea.util.slf4jLoggerOwner
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

/**
 * 设置超时方法 参见 [https://github.com/square/retrofit/issues/2982](https://github.com/square/retrofit/issues/2982)
 */
class TimeoutCallAdapterFactory : CallAdapter.Factory(), LoggerOwner by slf4jLoggerOwner<TimeoutCallAdapterFactory>() {

    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        val timeout = annotations.firstOrNull { it is Timeout } as? Timeout
        val delegate = retrofit.nextCallAdapter(this, returnType, annotations)

        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        return object : CallAdapter<Any, Call<Any>> {
            override fun responseType(): Type {
                return delegate.responseType()
            }

            override fun adapt(call: Call<Any>): Call<Any> {
                if (Looper.myLooper() == Looper.mainLooper()) {
                    val path = call.request().url().url().path
                    logger.error("禁止在主线程发起网络请求, path={}", path)
                    throw MainThreadNetworkError()
                }

                val path = call.request().url().url().path
                if (timeout != null) {
                    logger.debug("请求{}的超时时间被设置为{} {}", path, timeout.value, timeout.unit)
                    call.timeout().timeout(timeout.value, timeout.unit)
                } else {
                    logger.debug("请求{}的超时时间被设置为默认值{} s", path, RetrofitManager.DEFAULT_TIMEOUT.toLong())
                    call.timeout().timeout(RetrofitManager.DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                }
                return call
            }
        }
    }
}

class MainThreadNetworkError : Error()