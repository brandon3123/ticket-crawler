package model.generic

import model.Vendor


data class GamesWithSeats(
    val data: Map<Event, List<Listing>>,
    val vendor: Vendor
)