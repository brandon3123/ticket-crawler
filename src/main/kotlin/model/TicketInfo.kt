package model

import java.time.LocalDateTime

data class Ticket(
    val vendorEvent: Event,
    val vendorListing: Listing
)

data class TicketId(
    val time: LocalDateTime,
    val host: NHLTeam,
    val opponent: NHLTeam
)
