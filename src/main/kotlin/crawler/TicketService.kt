package crawler

import config.GameFilters

interface TicketService<T> {
    suspend fun calgaryFlamesTickets(gameFilters: GameFilters): T
    suspend fun calgaryWranglersTickets(gameFilters: GameFilters): T
}
