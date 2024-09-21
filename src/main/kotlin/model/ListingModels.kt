package model

import java.math.BigDecimal

data class Listing(
    val id: String,
    val price: BigDecimal,
    val spot: Spot,
    val numOfSeats: Int,
    val vendor: Vendor,
    val zone: String? = null,
    val sellOptions: List<Int>? = null
)

data class Spot(
    val row: Long,
    val section: String
)