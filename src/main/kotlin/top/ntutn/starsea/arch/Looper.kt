package top.ntutn.starsea.arch

class Looper {
    val mQueue = MessageQueue()

    companion object {
        private val sThreadLocal = ThreadLocal<Looper>()
        private var sMainLooper: Looper? = null

        fun myLooper() = sThreadLocal.get()

        fun mainLooper() = sMainLooper!!

        fun prepare() {
            if (sThreadLocal.get() != null) {
                throw IllegalStateException("looper prepared")
            }
            sThreadLocal.set(Looper())
        }

        fun prepareMainLooper() {
            prepare()
            synchronized(Looper::class) {
                if (sMainLooper != null) {
                    throw IllegalStateException("main looper prepared")
                }
                sMainLooper = myLooper()
            }
        }

        fun loop() {
            val me = myLooper() ?: throw IllegalStateException("you should call prepare first")
            while (true) {
                val message = me.mQueue.poll()
                message.target?.dispatchMessage(message)
            }
        }
    }
}