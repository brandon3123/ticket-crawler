package api.gametime

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import config.ExchangeRate
import model.gametime.Event
import model.gametime.Listing
import model.gametime.Price
import java.lang.reflect.Type
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
            .map { it.asJsonObject.get("event") }
            .map {
                // Get the event data
                val event = Gson().fromJson(it, Event::class.java)

                // Get the local date/time, and parse it
                val dateTime = it?.asJsonObject?.get("datetime_local")?.asString
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                val localDateTime = LocalDateTime.parse(dateTime, formatter)

                event.copy(
                    time = localDateTime
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

        val listings = listingsResponse
            .entrySet()
            .map { (_, listingJson) ->
                // Get the listing data
                val listing = Gson().fromJson(listingJson, Listing::class.java)

                // get USD price, by 2 decimal points
                val usdPrice = listing.price.total.movePointLeft(2)

                // Convert to CAD, if the exchange rate isn't present. Just use USD
                val total =
                    cadExchangeRate?.let { cadExchangeRate ->
                        val cadPrice = usdPrice * cadExchangeRate.toBigDecimal()
                        cadPrice.setScale(2, RoundingMode.HALF_UP)
                    } ?: usdPrice

                val price = Price(total)

                // Get the number of seats, per listing
                val seats = listingJson.asJsonObject["seats"].asJsonArray

                listing.copy(
                    price = price,
                    numOfSeats = seats.size()
                )
            }

        return ArrayList(listings)
    }
}