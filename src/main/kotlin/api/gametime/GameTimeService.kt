package api.gametime

import model.gametime.Event
import model.gametime.GamesWithSeats
import model.gametime.Listing
import model.gametime.isNotPressLevel

class GameTimeService(
    private val api: GameTimeApi
) {

    suspend fun calgaryFlamesGames(): List<Event> {
        return api.calgaryFlamesGames()
    }

    suspend fun seats(eventId: String): List<Listing> {
        return api.getListings(eventId)
            .filter { it.isNotPressLevel() }
            .sortedBy { it.price.total }
    }

    suspend fun seatsForGames(games: List<Event>): GamesWithSeats {
        val data = games
            .associateWith { seats(it.id).under(35) }
            .filter { (_, seats) -> seats.isNotEmpty() }

        return GamesWithSeats(data)
    }
}

fun List<Listing>.under(price: Long) =
    filter {
        it.price.total.longValueExact() <= price
    }
