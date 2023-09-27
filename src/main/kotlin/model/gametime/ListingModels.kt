package model.gametime

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

// comment for Bronson step 3
// Look at the Event class for an example of what to do
// -add @Serializable
// - Add @Contextual for each complication object property (price/spot). I'm not 100% sure what you'll need here.....
// You'll probably need to add a @Serializable annotation to the Price and Spot classes
data class Listing(
    val id: String,
    val price: Price,
    val spot: Spot,
    val numOfSeats: Int
)

fun Listing.isNotPressLevel() = !spot.section.startsWith("PL")

data class Price(
    val total: BigDecimal
)

data class Spot(
    val row: Long,
    val section: String,
    @SerializedName("view_url") val viewUrl: String
)