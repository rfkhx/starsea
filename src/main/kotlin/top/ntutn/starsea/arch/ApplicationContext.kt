package top.ntutn.starsea.arch

import top.ntutn.starsea.util.LoggerOwner
import top.ntutn.starsea.util.slf4jLoggerOwner
import java.io.File
import kotlin.concurrent.thread
import kotlin.system.exitProcess

object ApplicationContext: Thread.UncaughtExceptionHandler, LoggerOwner by slf4jLoggerOwner<ApplicationContext>() {
    private val handler by lazy { Handler(Looper.mainLooper()) }

    var exiting = false

    /**
     * 将系统标记为稳定运行状态。demand_lock.lock文件是由启动脚本生成的。
     */
    private fun markStable() {
        File("demand_lock.lock").takeIf { it.exists() }?.delete()
    }

    fun init() {
        // 主线程挂掉立即终止运行
        Thread.setDefaultUncaughtExceptionHandler(this)

        // 主线程长期无响应终止运行
        var bomb: Boolean
        thread {
            Thread.currentThread().name = "main-watcher"
            while (true) {
                bomb = true
                handler.post { bomb = false }
                Thread.sleep(6_000)
                if (bomb) {
                    logger.error("主线程长时间未响应！")
                    Thread.getAllStackTraces().forEach {
                        logger.error("dump thread {}, {}", it.key, it.value)
                    }
                    exitProcess(1)
                }
            }
        }

        // 运行5分钟后删除demand lock，这表示系统可以进入稳定运行阶段。如果刚启动就退出脚本不会重启程序
        handler.postDelayed({
            markStable()
        }, 5 * 60 * 1000)
    }

    /**
     * 将当前系统状态标记为稳定并停机。如果因为意外问题停机请exitProcess。
     */
    fun shutdown(delay: Long = 3000L) {
        markStable()
        exiting = true
        handler.postDelayed({
            exitProcess(0)
        }, delay)
    }

    override fun uncaughtException(thread: Thread, tr: Throwable) {
        logger.error("exception in thread {}", thread, tr)
        if (thread.name == "main") {
            logger.error("Main thread crashed!")
            exitProcess(1)
        }
    }
}