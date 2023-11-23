package crawler

import api.gametime.GameTimeService
import config.GameFilters
import model.gametime.GamesWithSeats

class GameTimeTicketService(
    private val gameTimeService: GameTimeService,
    private val gameTimeEmailBuilder: GameTimeEmailBuilder
) : TicketService<GamesWithSeats> {

    override suspend fun calgaryFlamesTickets(gameFilters: GameFilters): GamesWithSeats {
        // Fetch upcoming flames games, no press level
        val games = gameTimeService.calgaryFlamesGames(gameFilters.days)

        // Get the seats for each game, under x amount
        return gameTimeService.seatsForGames(games, gameFilters.maxPrice)
    }

    override suspend fun calgaryWranglersTickets(gameFilters: GameFilters): GamesWithSeats {
        // Fetch upcoming wranglers games, no press level
        val games = gameTimeService.calgaryWranglersGames()

        // Get the seats for each game, under x amount
        return gameTimeService.seatsForGames(games, gameFilters.maxPrice)
    }

    fun toEmailBody(gamesWithSeats: GamesWithSeats): String {
        return gameTimeEmailBuilder.toEmailBody(gamesWithSeats)
    }
}
