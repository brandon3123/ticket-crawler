package api.gametime

import config.GameFilters
import model.gametime.Event
import model.gametime.GamesWithSeats
import model.gametime.Listing
import java.time.LocalDate

class GameTimeService(
    private val api: GameTimeApi
) {

    suspend fun calgaryWranglersGames(): List<Event> {
        return api.calgaryWranglersGames()
    }

    suspend fun calgaryFlamesGames(gameFilters: GameFilters): List<Event> {
        return api
            .calgaryFlamesGames()
            .filterGames(gameFilters)
    }

    private suspend fun seats(eventId: String, gameFilters: GameFilters): List<Listing> {
        return api.getListings(eventId)
            .filterSeats(gameFilters)
            .sortedBy { it.price.total }
    }

    suspend fun seatsForGames(games: List<Event>, gameFilters: GameFilters): GamesWithSeats {
        val data = games
            .associateWith { seats(it.id, gameFilters) }
            .filter { (_, seats) -> seats.isNotEmpty() }

        return GamesWithSeats(data)
    }
}

private fun List<Event>.filterGames(gameFilters: GameFilters): List<Event> {
    // Add game filters here
    gameFilters.days?.let { this.forDates(it) }
    return this
}

private fun List<Listing>.filterSeats(gameFilters: GameFilters): List<Listing> {
    // Add seat filters here
    this.isNotPressLevel()
    this.under(gameFilters.maxPrice)
    return this
}

fun List<Listing>.under(price: Int) =
    filter {
        it.price.total.toLong() <= price
    }

fun List<Event>.forDates(gameDays: List<LocalDate>) =
    filter {
        gameDays.contains(it.time.toLocalDate())
    }

fun List<Listing>.isNotPressLevel() =
    filter {
        !it.spot.section.startsWith("PL")
    }
