package model


data class GamesWithSeats(
    val data: Map<Event, List<Listing>>,
    val vendor: Vendor
)

data class Tickets(
    val data: Map<TicketId, List<Listing>>
)