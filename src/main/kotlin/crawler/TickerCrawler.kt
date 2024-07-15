package crawler

import config.GameFilters
import emailer.EmailService
import kotlinx.coroutines.runBlocking
import model.TicketResults
import model.TicketWorker
import util.log

class TickerCrawler<T : TicketResults>(
    private val workers: List<TicketWorker<T>>,
    private val gameFilters: GameFilters,
    private val emailService: EmailService
) {

    private val flamesEmailSubject = "Flames Games Tickets - Ticket Crawler"
    private val wranglersEmailSubject = "Wrangles Games Tickets - Ticket Crawler"

    fun findTickets() {
        runBlocking {
            if (gameFilters.teams.flames) {
                findFlamesTickets()
            }

            if (gameFilters.teams.wranglers) {
                findWranglersTickets()
            }
        }
    }

    private suspend fun findFlamesTickets() {
        log("Looking for Calgary Flames tickets")

        val emailParts = workers.map { it.service.calgaryFlamesTickets(gameFilters) }.map { it.toString() }

        // Email them to me if found
        email(
            subject = flamesEmailSubject,
            emailParts = emailParts
        )
    }

    private suspend fun findWranglersTickets() {
        log("Looking for Calgary Wranglers tickets")

        val emailParts = workers.map {
            val tickets = it.service.calgaryWranglersTickets(gameFilters)
            it.emailer.toEmailBody(tickets)
        }

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