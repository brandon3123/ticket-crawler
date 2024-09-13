package api.fansfirst

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import config.ExchangeRate
import model.generic.Event
import model.generic.Listing
import model.generic.Opponent
import model.generic.Spot
import model.generic.opponent
import java.lang.reflect.Type
import java.math.BigDecimal
import java.math.RoundingMode
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
                val opponent = eventJson?.asJsonObject?.get("opponent")?.asString?.opponent() ?: Opponent.UNKNOWN

                Event(
                    id = id,
                    name = name,
                    time = localDateTime,
                    opponent = opponent
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

                // get USD price, by 2 decimal points
                val usdPrice = listingJson.asJsonObject["price"]
                    ?.asBigDecimal?.movePointLeft(2)
                    ?: BigDecimal.valueOf(0)

                // Convert to CAD, if the exchange rate isn't present. Just use USD
                val total =
                    cadExchangeRate?.let { cadExchangeRate ->
                        val cadPrice = usdPrice * cadExchangeRate.toBigDecimal()
                        cadPrice.setScale(2, RoundingMode.HALF_UP)
                    } ?: usdPrice

                // Get the spot
                val row = listingJson.asJsonObject["row"].asLong
                val section = listingJson.asJsonObject["zoneNo"]?.asString ?: ""

                val spot = Spot(row, section, "asdasd")

                val numOfSeats = listingJson.asJsonObject["sellOption"]?.asInt ?: 0

                Listing(
                    id = id,
                    price = total,
                    spot = spot,
                    numOfSeats = numOfSeats
                )
            }

        return ArrayList(listings)
    }
}