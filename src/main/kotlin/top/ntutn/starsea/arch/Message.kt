package top.ntutn.starsea.arch

import kotlinx.coroutines.Runnable
import java.util.concurrent.Delayed
import java.util.concurrent.TimeUnit
import kotlin.math.abs

/**
 * 参考学习安卓looper和handler机制
 */
data class Message(
    val what: Int,
    val arg: Int = -1,
    val payload: Any? = null,
    var time: Long = 0L,
    var target: Handler? = null,
    val callback: Runnable? = null
): Delayed {
    override fun compareTo(other: Delayed?): Int {
        val value1 = this.getDelay(TimeUnit.MILLISECONDS)
        val value2 = (other?.getDelay(TimeUnit.MILLISECONDS)?: 0L)
        return (value1 - value2).getSymbolInt()
    }

    override fun getDelay(unit: TimeUnit): Long {
        val diffTime = time - System.currentTimeMillis()
        return unit.convert(diffTime, TimeUnit.MILLISECONDS)
    }

    /*
     * 安全地提取符号，将long转换成 -1 0 1
     */
    private fun Long.getSymbolInt(): Int {
        val value = this
        if (value == 0L) {
            return 0
        }
        return (value / abs(value)).toInt()
    }
}