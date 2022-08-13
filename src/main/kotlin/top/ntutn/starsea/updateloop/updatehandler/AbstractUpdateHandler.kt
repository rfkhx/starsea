package top.ntutn.starsea.updateloop.updatehandler

import top.ntutn.starsea.bean.UpdateBean
import top.ntutn.starsea.util.LoggerOwner
import top.ntutn.starsea.util.slf4jLoggerOwner

/**
 * 责任链模式，根据消息类型处理update
 */
abstract class AbstractUpdateHandler {
    fun handleUpdate(update: UpdateBean): Boolean {
        return dispatchUpdate(update) || nextHandler?.handleUpdate(update) == true
    }

    protected abstract fun dispatchUpdate(update: UpdateBean): Boolean

    abstract val nextHandler: AbstractUpdateHandler?
}

class StubUpdateHandler: AbstractUpdateHandler(), LoggerOwner by slf4jLoggerOwner<StubUpdateHandler>() {
    override fun dispatchUpdate(update: UpdateBean): Boolean {
        logger.warn("Missing handler for update {}", update)
        return false
    }

    override val nextHandler: AbstractUpdateHandler? = null
}