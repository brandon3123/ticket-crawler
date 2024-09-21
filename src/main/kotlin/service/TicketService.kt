package service

import config.GameFilters
import model.GamesWithSeats

interface TicketService {
    suspend fun calgaryFlamesTickets(gameFilters: GameFilters): GamesWithSeats
    suspend fun calgaryWranglersTickets(gameFilters: GameFilters): GamesWithSeats
}
