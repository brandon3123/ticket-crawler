package model.generic

import model.TicketResults

data class GamesWithSeats(
    val data: Map<Event, List<Listing>>
) : TicketResults