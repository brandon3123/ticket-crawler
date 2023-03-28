package api.gametime

import model.gametime.Event
import model.gametime.Listing

class GameTimeService(
    private val api: GameTimeApi
) {

    suspend fun calgaryFlamesGames(): List<Event> {
        return api.calgaryFlamesGames()
    }

    suspend fun seats(): List<Listing> {
        return api.getListings("62c612b069cc920001347734")
    }
}