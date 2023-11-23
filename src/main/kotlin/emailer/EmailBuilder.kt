package emailer

interface EmailBuilder<T> {
    fun toEmailBody(data: T): String
}
