package crawler

interface EmailBuilder<T> {
    fun toEmailBody(data: T): String
}
