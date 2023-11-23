package crawler

import config.GameFilters
import emailer.EmailService
import kotlinx.coroutines.runBlocking
import util.log

class TickerCrawler(
    private val gameTimeTicketService: GameTimeTicketService,
    private val gameFilters: GameFilters,
    private val emailService: EmailService
) {

    private val flamesEmailSubject = "Flames Games Tickets - Ticket Crawler"
    private val wranglersEmailSubject = "Wrangles Games Tickets - Ticket Crawler"

    fun findTickets() {
        runBlocking {
            findFlamesTickets()
            findWranglersTickets()
        }
    }

    suspend fun findFlamesTickets() {
        log("Looking for Calgary Flames tickets")

        val gameTimeTickets = gameTimeTicketService.calgaryFlamesTickets(gameFilters)
        val gameTimeTicketsAsEmail = gameTimeTicketService.toEmailBody(gameTimeTickets)

        val emailParts = listOf(
            gameTimeTicketsAsEmail
        )

        // Email them to me if found
        email(
            subject = flamesEmailSubject,
            emailParts = emailParts
        )
    }

    suspend fun findWranglersTickets() {
        log("Looking for Calgary Wranglers tickets")

        val gameTimeTickets = gameTimeTicketService.calgaryWranglersTickets(gameFilters)
        val gameTimeTicketsAsEmail = gameTimeTicketService.toEmailBody(gameTimeTickets)

        val emailParts = listOf(
            gameTimeTicketsAsEmail
        )

        // Email them to me if found
        email(
            subject = wranglersEmailSubject,
            emailParts = emailParts
        )
    }

    private fun email(subject: String, emailParts: List<String>) {
        if (emailParts.isNotEmpty()) {
            emailService.sendEmailNotification(
                subject = subject,
                emailParts = emailParts
            )
        } else {
            log("No tickets found")
        }
    }

}