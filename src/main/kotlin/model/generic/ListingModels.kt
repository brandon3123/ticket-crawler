package model.generic

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Listing(
    val id: String,
    val price: BigDecimal,
    val spot: Spot,
    val numOfSeats: Int,
    val zone: String
)

data class Spot(
    val row: Long,
    val section: String,
    @SerializedName("view_url") val viewUrl: String
)