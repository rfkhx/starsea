package top.ntutn.starsea.arch

import com.google.auto.service.AutoService
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.internal.MainDispatcherFactory
import kotlin.coroutines.CoroutineContext

@OptIn(InternalCoroutinesApi::class)
@AutoService(MainDispatcherFactory::class)
class MainDispatcherFactoryImpl: MainDispatcherFactory {
    override val loadPriority: Int
        get() = 0

    override fun createDispatcher(allFactories: List<MainDispatcherFactory>): MainCoroutineDispatcher {
        return MainDispatcher()
    }
}

class MainDispatcher: MainCoroutineDispatcher() {
    private val handler = Handler(Looper.mainLooper())

    override val immediate: MainCoroutineDispatcher
        get() = this

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        handler.sendMessage(Message(0, callback = block))
    }
}