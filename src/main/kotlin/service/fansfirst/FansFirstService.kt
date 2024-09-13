package service.fansfirst

import api.fansfirst.FansFirstApi
import config.GameFilters
import model.Vendor
import model.generic.Event
import model.generic.Listing
import model.generic.GamesWithSeats
import model.generic.Opponent
import java.time.LocalDate

class FansFirstService(
    private val api: FansFirstApi
) {

    suspend fun calgaryFlamesGames(gameFilters: GameFilters): List<Event> {
        return api
            .calgaryFlamesGames()
            .filterGames(gameFilters)
    }

    private suspend fun seats(eventId: String, gameFilters: GameFilters): List<Listing> {
        return api.getListings(eventId)
            .filterSeats(gameFilters)
            .sortedBy { it.price }
    }

    suspend fun seatsForGames(games: List<Event>, gameFilters: GameFilters): GamesWithSeats {
        val data = games
            .associateWith { seats(it.id, gameFilters) }
            .filter { (_, seats) -> seats.isNotEmpty() }

        return GamesWithSeats(data, Vendor.FANS_FIRST)
    }

    private fun List<Event>.filterGames(gameFilters: GameFilters): List<Event> {
        var filteredGames = this

        // add filters here
        gameFilters.days?.let { filteredGames = filteredGames.forDates(it) }
        gameFilters.opponents?.let { filteredGames = filteredGames.vsing(it) }

        return filteredGames
    }

    private fun List<Listing>.filterSeats(gameFilters: GameFilters): List<Listing> {
        var filteredSeats = this

        // add filters here
        filteredSeats = filteredSeats
            .notPressLevel()
            .seats(gameFilters.seats)
            .under(gameFilters.maxPrice)

        return filteredSeats
    }

    fun List<Event>.forDates(gameDays: List<LocalDate>) =
        filter {
            gameDays.contains(it.time.toLocalDate())
        }

    fun List<Event>.vsing(opponents: List<Opponent>) =
        filter {
            opponents.contains(it.opponent)
        }

    private fun List<Listing>.notPressLevel() =
        filter {
            it.zone != "Press Level"
        }

    private fun List<Listing>.under(price: Int) =
        filter {
            it.price.toLong() <= price
        }

    private fun List<Listing>.seats(numOfSeats: Int) =
        filter {
            it.numOfSeats == numOfSeats
        }
}