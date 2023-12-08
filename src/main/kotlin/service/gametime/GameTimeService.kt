package service.gametime

import api.gametime.GameTimeApi
import config.GameFilters
import model.gametime.Event
import model.gametime.GamesWithSeats
import model.gametime.Listing
import model.gametime.Opponent
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

fun List<Listing>.under(price: Int) =
    filter {
        it.price.total.toLong() <= price
    }

fun List<Listing>.seats(numOfSeats: Int) =
    filter {
        it.numOfSeats == numOfSeats
    }

fun List<Event>.forDates(gameDays: List<LocalDate>) =
    filter {
        gameDays.contains(it.time.toLocalDate())
    }

fun List<Event>.vsing(opponents: List<Opponent>) =
    filter {
        opponents.contains(it.opponent)
    }

fun List<Listing>.notPressLevel() =
    filter {
        !it.spot.section.startsWith("PL")
    }
