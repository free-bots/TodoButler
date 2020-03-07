package to.freebots.todobutler.common

class Event<E>(val value: E) {
    private var consumed = false
    fun peek(): E = value
    fun consume():E? {
        return if(consumed) null else value
    }
}