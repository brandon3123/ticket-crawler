package model

import java.math.BigDecimal

data class Listing(
    val id: String,
    val price: BigDecimal,
    val spot: Spot,
    val numOfSeats: Int,
    val zone: String? = null,
    val vendor: Vendor
)

data class Spot(
    val row: Long,
    val section: String
)