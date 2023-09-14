package routes

import api.gametime.GameTimeService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.games(
    gameTimeService: GameTimeService
) {
    get("api/games") {
        val games = gameTimeService.calgaryFlamesGames()
        call.respond(games)
    }
}

// Make a seats route here
// copy paste above, get some games, and call the seats functions from GameTimeService for an event id.
