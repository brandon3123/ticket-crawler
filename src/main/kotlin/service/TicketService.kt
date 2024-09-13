package service

import config.GameFilters
import model.generic.GamesWithSeats

interface TicketService {
    suspend fun calgaryFlamesTickets(gameFilters: GameFilters): GamesWithSeats
    suspend fun calgaryWranglersTickets(gameFilters: GameFilters): GamesWithSeats
}
