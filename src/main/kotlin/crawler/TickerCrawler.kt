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
                    val games = gameTimeService.calgaryFlamesGames()

                    val gamesWithSeats =
                        games
                            .associateWith { game ->
                                gameTimeService.seats(game.id).under(40)
                            }
                            .filter { (_, seats) -> seats.isNotEmpty() }

                    emailService.sendEmailNotification(gamesWithSeats)

                    delay(fifteenMinutes)
                }

            }
        }

    }

}