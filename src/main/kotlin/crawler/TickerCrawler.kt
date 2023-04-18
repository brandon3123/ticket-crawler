package crawler

import api.gametime.GameTimeService
import emailer.EmailService
import kotlinx.coroutines.runBlocking
import model.gametime.GamesWithSeats
import util.log

class TickerCrawler(
    private val gameTimeService: GameTimeService,
    private val emailService: EmailService
) {

    private val maxPrice = 30
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

        // Fetch upcoming flames games, no press level
        val games = gameTimeService.calgaryFlamesGames()

        // Get the seats for each game, under x amount
        val gamesWithSeats = gameTimeService.seatsForGames(games, maxPrice)

        // Email them to me if found
        emailGamesWithSeats(
            subject = flamesEmailSubject,
            gamesWithSeats = gamesWithSeats
        )
    }

    suspend fun findWranglersTickets() {
        log("Looking for Calgary Wranglers tickets")

        // Fetch upcoming wranglers games, no press level
        val games = gameTimeService.calgaryWranglersGames()

        // Get the seats for each game, under x amount
        val gamesWithSeats = gameTimeService.seatsForGames(games, maxPrice)

        // Email them to me if found
        emailGamesWithSeats(
            subject = wranglersEmailSubject,
            gamesWithSeats = gamesWithSeats
        )
    }

    private fun emailGamesWithSeats(subject: String, gamesWithSeats: GamesWithSeats) {
        if (gamesWithSeats.data.isNotEmpty()) {
            emailService.sendEmailNotification(
                subject = subject,
                gamesWithSeats = gamesWithSeats
            )
        } else {
            log("No tickets found")
        }
    }

}