package service.gametime

import api.gametime.GameTimeApi
import config.GameFilters
import model.Vendor
import model.Event
import model.GamesWithSeats
import model.Listing
import model.NHLTeam as GenericOpponent
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
            .sortedBy { it.price }
    }

    suspend fun seatsForGames(games: List<Event>, gameFilters: GameFilters): GamesWithSeats {
        val data = games
            .associateWith { seats(it.id, gameFilters) }
            .filter { (_, seats) -> seats.isNotEmpty() }

        return GamesWithSeats(data, Vendor.GAME_TIME)
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
        it.price.toLong() <= price
    }

fun List<Listing>.seats(numOfSeats: Int) =
    filter {
        it.numOfSeats == numOfSeats
    }

fun List<Event>.forDates(gameDays: List<LocalDate>) =
    filter {
        gameDays.contains(it.time.toLocalDate())
    }

fun List<Event>.vsing(opponents: List<GenericOpponent>) =
    filter {
        opponents.contains(it.team)
    }

fun List<Listing>.notPressLevel() =
    filter {
        !it.spot.section.startsWith("PL")
    }
