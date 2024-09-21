package api.gametime

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import config.ExchangeRate
import model.Event
import model.Listing
import model.NHLTeam
import model.Spot
import model.Vendor
import java.lang.reflect.Type
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EventsDeserializer : JsonDeserializer<ArrayList<Event>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ArrayList<Event> {
        val eventsResponseJson = json?.asJsonObject?.getAsJsonArray("events") ?: JsonArray()

        val events = eventsResponseJson
            .map {
                Pair(
                    it.asJsonObject["event"],
                    it.asJsonObject["performers"].asJsonArray
                )
            }
            .map { (eventJson, performerJson) ->

                // Get the event data
                val event = Gson().fromJson(eventJson, Event::class.java)

                // Get the local date/time, and parse it
                val dateTime = eventJson?.asJsonObject?.get("datetime_local")?.asString
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                val localDateTime = LocalDateTime.parse(dateTime, formatter)

                // Remove Calgary performers
                performerJson?.removeAll { performer -> performer?.asJsonObject?.get("name")?.asString?.contains("Calgary") == true }

                // Get the abbreviation for the opponent of the Calgary based team
                val team = performerJson[0].asJsonObject?.get("abbrev")?.asString?.let {
                    try {
                        NHLTeam.valueOf(it)} catch (e: Exception) {
                        NHLTeam.UNKNOWN}
                } ?: NHLTeam.UNKNOWN

                event.copy(
                    time = localDateTime,
                    team = team,
                    vendor = Vendor.GAME_TIME
                )
            }

        return ArrayList(events)
    }
}

class ListingsDeserializer(
    exchangeRate: ExchangeRate
) : JsonDeserializer<ArrayList<Listing>> {
    private val cadExchangeRate = exchangeRate.cad()

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ArrayList<Listing> {
        val listingsResponse = json?.asJsonObject?.getAsJsonObject("listings") ?: JsonObject()

        println("game time list")

        val listings = listingsResponse
            .entrySet()
            .map { (_, listingJson) ->
                val listing = listingJson?.asJsonObject

                // ID
                val id = listing
                    ?.getAsJsonPrimitive("id")
                    ?.asString
                    ?: ""

                // spot
                val spot = Gson().fromJson(listingJson, Spot::class.java)

                // get USD price, by 2 decimal points
                val usdPrice = listing
                    ?.getAsJsonObject("price")
                    ?.getAsJsonPrimitive("total")
                    ?.asBigDecimal?.movePointLeft(2)
                    ?: BigDecimal.valueOf(0)

                // Convert to CAD, if the exchange rate isn't present. Just use USD
                val total =
                    cadExchangeRate?.let { cadExchangeRate ->
                        val cadPrice = usdPrice * cadExchangeRate.toBigDecimal()
                        cadPrice.setScale(2, RoundingMode.HALF_UP)
                    } ?: usdPrice

                // Get the number of seats, per listing
                val numOfSeats = listingJson.asJsonObject["seats"].asJsonArray.size()

                Listing(
                    id = id,
                    spot = spot,
                    price = total,
                    numOfSeats = numOfSeats,
                    vendor = Vendor.GAME_TIME
                )
            }

        return ArrayList(listings)
    }
}