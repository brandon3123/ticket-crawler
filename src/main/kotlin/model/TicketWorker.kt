package model

import emailer.EmailBuilder
import service.TicketService

data class TicketWorker<T: TicketResults>(
    val service: TicketService<T>,
    val emailer: EmailBuilder<T>
)
