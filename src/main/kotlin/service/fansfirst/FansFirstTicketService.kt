package service.fansfirst

import config.GameFilters
import model.fansfirst.GamesWithSeats
import service.TicketService

class FansFirstTicketService(
    private val fansFirstService: FansFirstService
) : TicketService<GamesWithSeats> {

    override suspend fun calgaryFlamesTickets(gameFilters: GameFilters): GamesWithSeats {
        // Fetch upcoming flames games, no press level
        val games = fansFirstService.calgaryFlamesGames(gameFilters)

        // Get the seats for each game, under x amount
        return fansFirstService.seatsForGames(games, gameFilters)
    }

    override suspend fun calgaryWranglersTickets(gameFilters: GameFilters): GamesWithSeats {
        //todo
        return GamesWithSeats(emptyMap())
    }
}
