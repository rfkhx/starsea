package top.ntutn.starsea.arch

open class Handler(looper: Looper? = null) {
    private var mLooper: Looper
    private var mQueue: MessageQueue
    private var mCallback: ((Message) -> Unit)? = null

    init {
        mLooper = looper ?: Looper.myLooper() ?: throw IllegalStateException("looper not prepared")
        mQueue = mLooper.mQueue
    }

    fun sendMessage(msg: Message) = sendMessageDelay(msg, 0L)

    fun sendMessageDelay(msg: Message, delay: Long) {
        require(delay >= 0L) { "delay cannot be negative" }
        msg.target = this
        mQueue.enqueue(msg, System.currentTimeMillis() + delay)
    }

    fun post(block: () -> Unit) {
        sendMessage(Message(0, callback = block))
    }

    fun postDelayed(block: () -> Unit, delay: Long) {
        sendMessageDelay(Message(0, callback = block), delay)
    }

    fun dispatchMessage(message: Message) {
        message.callback?.run() ?: mCallback?.invoke(message) ?: handleMessage(message)
    }

    fun clearMessages() {
        mQueue.clearMessages(this)
    }

    protected open fun handleMessage(message: Message) {}
}