package crawler

import api.gametime.GameTimeService
import api.gametime.under
import emailer.EmailService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TickerCrawler(
    private val gameTimeService: GameTimeService,
    private val emailService: EmailService
) {

    private val fifteenMinutes = 15L * 60L * 1000L

    fun findFlamesTickets() {
        runBlocking {
            launch {
                repeat(Int.MAX_VALUE) {
                    println("Looking for tickets....")

                    // Fetch upcoming flames games, no press level
                    val games = gameTimeService.calgaryFlamesGames()

                    // Get the seats for each game, under x amount
                    val gamesWithSeats = games
                        .associateWith { gameTimeService.seats(it.id).under(35) }
                        .filter { (_, seats) -> seats.isNotEmpty() }

                    // Email them to me if found
                    if (gamesWithSeats.isNotEmpty()) {
                        emailService.sendEmailNotification(gamesWithSeats)
                    } else {
                        println("No tickets found.....")
                    }

                    // Runs this code every 15 minutes
                    delay(fifteenMinutes)
                }

            }
        }

    }

}