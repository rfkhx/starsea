package top.ntutn.starsea.arch

import java.util.concurrent.DelayQueue

class MessageQueue {
    private val mQueue = DelayQueue<Message>()

    fun enqueue(msg: Message) {
        msg.time = 0L
        mQueue.put(msg)
    }

    fun enqueue(msg: Message, time: Long) {
        msg.time = time
        mQueue.put(msg)
    }

    fun poll(): Message {
        return mQueue.take()
    }

    fun clearMessages(handler: Handler) {
        mQueue.removeIf { it.target == handler }
    }
}