package emailer

import model.TicketResults

fun interface EmailBuilder<T: TicketResults> {
    fun toEmailBody(data: T): String
}
