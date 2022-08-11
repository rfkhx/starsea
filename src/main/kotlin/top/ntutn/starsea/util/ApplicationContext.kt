package top.ntutn.starsea.util

import top.ntutn.starsea.arch.Handler
import top.ntutn.starsea.arch.Looper
import java.io.File
import kotlin.system.exitProcess

object ApplicationContext {
    private val handler by lazy { Handler(Looper.mainLooper()) }

    fun init() {
        // 运行5分钟后删除demand lock，这表示系统可以进入稳定运行阶段。如果刚启动就退出脚本不会重启程序
        handler.postDelayed({
            File("demand_lock.lock").takeIf { it.exists() }?.delete()
        }, 5 * 60 * 1000)
    }

    fun shutdown(delay: Long = 3000L) {
        handler.postDelayed({
            exitProcess(0)
        }, delay)
    }
}