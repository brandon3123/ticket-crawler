package api.fansfirst

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import config.ExchangeRate
import model.Event
import model.Listing
import model.NHLTeam
import model.Spot
import model.Vendor
import model.opponent
import java.lang.reflect.Type
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.util.TimeZone

class EventsDeserializer : JsonDeserializer<ArrayList<Event>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ArrayList<Event> {
        val eventsResponseJson = json?.asJsonArray ?: JsonArray()

        val events = eventsResponseJson
            .map { eventJson ->

                // Get the event data
                val id = eventJson?.asJsonObject?.get("id")?.asString!!
                val name = eventJson?.asJsonObject?.get("longName")?.asString!!

                // Get the local date/time, and parse it
                val dateTime = eventJson?.asJsonObject?.get("date")?.asLong!!
                val localDateTime =
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTime), TimeZone.getDefault().toZoneId())

                // Get the abbreviation for the opponent of the Calgary based team
                val team = eventJson?.asJsonObject?.get("opponent")?.asString?.opponent() ?: NHLTeam.UNKNOWN

                Event(
                    id = id,
                    name = name,
                    time = localDateTime,
                    team = team,
                    vendor = Vendor.FANS_FIRST
                )
            }

        return ArrayList(events)
    }
}

class ListingsDeserializer(exchangeRate: ExchangeRate) : JsonDeserializer<ArrayList<Listing>> {

    private val cadExchangeRate = exchangeRate.cad()

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ArrayList<Listing> {
        val listingsResponse = json?.asJsonArray ?: JsonArray()

        val listings = listingsResponse
            .map { listingJson ->
                // Get the listing data

                val id = listingJson.asJsonObject["seatId"].asString

                // get price
                val price = listingJson.asJsonObject["price"]?.asBigDecimal ?: BigDecimal.ZERO

                // Get the spot
                val row = listingJson.asJsonObject["row"].asLong
                val section = listingJson.asJsonObject["zoneNo"]?.asString ?: ""

                val spot = Spot(row, section)

                val numOfSeats = listingJson.asJsonObject["noOfSeats"]?.asInt ?: 0

                val sellOptionsJson = listingJson.asJsonObject["quantityOptions"].asJsonArray ?: JsonArray()

                val sellOptions = sellOptionsJson.map { it.asInt }

                Listing(
                    id = id,
                    price = price,
                    spot = spot,
                    numOfSeats = numOfSeats,
                    vendor = Vendor.FANS_FIRST,
                    sellOptions = sellOptions
                )
            }

        return ArrayList(listings)
    }
}