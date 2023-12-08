package model.fansfirst

import java.math.BigDecimal

data class Listing(
    val seatId: String,
    val price: BigDecimal,
    val spot: Spot,
    val zone: String,
    val noOfSeats: Int
)

data class Spot(
    val row: String,
    val section: Long,
)