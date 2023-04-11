package crawler

import api.gametime.GameTimeService
import emailer.EmailService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalTime

class TickerCrawler(
    private val gameTimeService: GameTimeService,
    private val emailService: EmailService
) {

    private val fifteenMinutes = 15L * 60L * 1000L
    private val maxPrice = 35

    fun findFlamesTickets() {
        runBlocking {
            launch {
                while(true) {
                    val now = LocalTime.now()

                    println("$now: *** Looking for tickets ***")

                    // Fetch upcoming flames games, no press level
                    val games = gameTimeService.calgaryFlamesGames()

                    // Get the seats for each game, under x amount
                    val gamesWithSeats = gameTimeService.seatsForGames(games, maxPrice)

                    // Email them to me if found
                    if (gamesWithSeats.data.isNotEmpty()) {
                        emailService.sendEmailNotification(gamesWithSeats)
                    } else {
                        println("$now: *** No tickets found ***")
                    }

                    // Runs this code every 15 minutes
                    delay(fifteenMinutes)
                }

            }
        }

    }

}