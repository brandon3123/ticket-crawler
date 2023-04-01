package api.gametime

import model.gametime.Event
import model.gametime.Listing

class GameTimeService(
    private val api: GameTimeApi
) {

    suspend fun calgaryFlamesGames(): List<Event> {
        return api.calgaryFlamesGames()
    }

    suspend fun seats(eventId: String): List<Listing> {
        return api.getListings(eventId)
    }
}

fun List<Listing>.under(price: Long) =
    filter {
        it.price.total.longValueExact() <= price
    }
