package api.gametime

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import model.gametime.Event
import model.gametime.Listing
import model.gametime.Price
import java.lang.reflect.Type
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

class ListingsDeserializer : JsonDeserializer<ArrayList<Listing>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ArrayList<Listing> {
        val listingsResponse = json?.asJsonObject?.getAsJsonObject("listings") ?: JsonObject()

        val listings = listingsResponse
            .entrySet()
            .map { (_, listingJson ) ->
                // Get the listing data
                val listing = Gson().fromJson(listingJson, Listing::class.java)

                // Update the price into something with 2 decimals
                val moneyFormat = Price(listing.price.total.movePointLeft(2))

                // Get the number of seats, per listing
                val seats = listingJson.asJsonObject.get("seats").asJsonArray

                listing.copy(
                    price = moneyFormat,
                    numOfSeats = seats.size()
                )
            }

        return ArrayList(listings)
    }
}