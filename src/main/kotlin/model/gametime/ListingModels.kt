package model.gametime

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Listing(
    val id: String,
    val price: Price,
    val spot: Spot,
    val numOfSeats: Int
)

data class Price(
    val total: BigDecimal
)

data class Spot(
    val row: Long,
    val section: String,
    @SerializedName("view_url") val viewUrl: String
)