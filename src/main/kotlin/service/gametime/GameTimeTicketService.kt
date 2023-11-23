package service.gametime

import config.GameFilters
import model.gametime.GamesWithSeats
import service.TicketService

class GameTimeTicketService(
    private val gameTimeService: GameTimeService
) : TicketService<GamesWithSeats> {

    override suspend fun calgaryFlamesTickets(gameFilters: GameFilters): GamesWithSeats {
        // Fetch upcoming flames games, no press level
        val games = gameTimeService.calgaryFlamesGames(gameFilters)

        // Get the seats for each game, under x amount
        return gameTimeService.seatsForGames(games, gameFilters)
    }

    override suspend fun calgaryWranglersTickets(gameFilters: GameFilters): GamesWithSeats {
        // Fetch upcoming wranglers games, no press level
        val games = gameTimeService.calgaryWranglersGames()

        // Get the seats for each game, under x amount
        return gameTimeService.seatsForGames(games, gameFilters)
    }
}
