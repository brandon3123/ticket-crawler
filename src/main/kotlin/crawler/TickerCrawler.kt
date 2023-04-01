package crawler

import api.gametime.GameTimeService
import api.gametime.under
import emailer.Emailer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.format.DateTimeFormatter

class TickerCrawler(
    private val gameTimeService: GameTimeService,
    private val emailer: Emailer
) {

    private val fifteenMinutes = 10L * 1000L
    private val niceDatePattern = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' h:mm a")
    fun findFlamesTickets() {
        runBlocking {
            launch {
                repeat(Int.MAX_VALUE) {
                    val games = gameTimeService.calgaryFlamesGames()

                    val cheapSeats = games.flatMap { game ->
                        val seats = gameTimeService.seats(game.id).under(40)

                        if (seats.isNotEmpty()) {
//                            println("Found seats under $20 for game ${game.name} at ${niceDatePattern.format(game.time)}")

                            seats

//                            seats.forEach { seat ->
//                                println("Found seat in row ${seat.spot.row}, section ${seat.spot.section}")
//                            }
                        } else {
                            emptyList()
                        }

                    }

                    emailer.sendEmailNotification(cheapSeats)

                    delay(fifteenMinutes)
                }

            }
        }

    }

}