package top.ntutn.starsea.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface LoggerOwner {
    val logger: Logger
}

class Slf4jLoggerOwner(private val tag: String) : LoggerOwner {
    private val _logger by lazy {
        LoggerFactory.getLogger(tag)
    }

    override val logger: Logger
        get() = _logger
}

/**
 * 创建logger对象。
 * 用于方法委托使用。
 */
inline fun <reified T> slf4jLoggerOwner(): Slf4jLoggerOwner = Slf4jLoggerOwner(T::class.java.canonicalName?:"NO_NAME")

/**
 * 创建logger对象
 * 用于直接使用
 */
inline fun <reified T> slf4jLogger(): Logger = slf4jLoggerOwner<T>().logger
