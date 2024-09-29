package crawler

import config.GameFilters
import emailer.EmailService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import model.TicketWorker
import model.NHLTeam
import model.Ticket
import model.TicketId
import util.log

class TickerCrawler(
    private val workers: List<TicketWorker>,
    private val gameFilters: GameFilters,
    private val emailService: EmailService
) {

    private val flamesEmailSubject = "Flames Games Tickets - For the Red Mile Gang"
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

        // Fetch tickets from vendors async
        val ticketsForVendors = coroutineScope {
            workers.map {
                async {
                    it.service.calgaryFlamesTickets(gameFilters)
                }
            }.awaitAll()
        }

        val combinedTickets = mutableMapOf<TicketId, List<Ticket>>()

        ticketsForVendors.flatMap { (data, _) ->
            data.toList()
        }.map { (event, listings) ->
            val ticketId = TicketId(event.time, NHLTeam.CGY, event.team)
            val tickets = combinedTickets[ticketId]

            if (tickets != null) {
                val toAdd = listings.map { Ticket(event, it) }
                val combined = tickets + toAdd
                combinedTickets[ticketId] = combined
            } else {
                combinedTickets[ticketId] = listings.map { Ticket(event, it) }
            }
        }

        val results = combinedTickets.map { (id, tickets) ->
            id to tickets.sortedBy { it.vendorListing.price }
        }.toMap()

        // Email them to me if found
        email(
            subject = flamesEmailSubject,
            tickets = results
        )
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

    private fun email(subject: String, tickets: Map<TicketId, List<Ticket>>) {
        if (tickets.isNotEmpty()) {
            emailService.sendEmailNotification(
                subject = subject,
                tickets = tickets
            )
        } else {
            log("No tickets found")
        }
    }

}