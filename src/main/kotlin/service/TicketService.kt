package service

import config.GameFilters
import model.TicketResults

interface TicketService<T: TicketResults> {
    suspend fun calgaryFlamesTickets(gameFilters: GameFilters): T
    suspend fun calgaryWranglersTickets(gameFilters: GameFilters): T
}
