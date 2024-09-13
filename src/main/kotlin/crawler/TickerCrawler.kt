package crawler

import config.GameFilters
import emailer.EmailService
import emailer.EmailBuilder
import kotlinx.coroutines.runBlocking
import model.TicketWorker
import model.generic.GamesWithSeats
import util.log

class TickerCrawler(
    private val workers: List<TicketWorker>,
    private val gameFilters: GameFilters,
    private val emailService: EmailService,
    private val emailBuilder: EmailBuilder
) {

    private val flamesEmailSubject = "Flames Games Tickets - Ticket Crawler"
    private val wranglersEmailSubject = "Wrangles Games Tickets - Ticket Crawler"

    fun findTickets() {
        runBlocking {
            if (gameFilters.teams.flames) {
                val tickets = findFlamesTickets()
                val email = emailBuilder.toEmailBody(tickets)
                email(flamesEmailSubject, listOf(email))
            }

            if (gameFilters.teams.wranglers) {
                findWranglersTickets()
            }
        }
    }

    private suspend fun findFlamesTickets(): List<GamesWithSeats> {
        log("Looking for Calgary Flames tickets")

        val emailParts = workers.map {
            it.service.calgaryFlamesTickets(gameFilters)
        }

        return emailParts


        // Email them to me if found
//        email(
//            subject = flamesEmailSubject,
//            emailParts = emailParts
//        )
    }

    private suspend fun findWranglersTickets() {
        log("Looking for Calgary Wranglers tickets")

        val emailParts = workers.map {
            val tickets = it.service.calgaryWranglersTickets(gameFilters)
//            it.emailer.toEmailBody(tickets)
        }

        // Email them to me if found
//        email(
//            subject = wranglersEmailSubject,
//            emailParts = emailParts
//        )
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