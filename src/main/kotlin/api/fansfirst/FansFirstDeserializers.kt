package api.fansfirst

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import model.fansfirst.Event
import model.fansfirst.Listing
import model.fansfirst.Spot
import java.lang.reflect.Type
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
                val event = Gson().fromJson(eventJson, Event::class.java)

                // Get the local date/time, and parse it
                val dateTime = eventJson?.asJsonObject?.get("date")?.asLong!!
                val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTime), TimeZone.getDefault().toZoneId())

                // Get the abbreviation for the opponent of the Calgary based team
//                val opponent = performerJson[0].asJsonObject?.get("abbrev")?.asString?.let {
//                    Opponent.valueOf(it)
//                } ?: Opponent.UNKNOWN

                event.copy(
                    time = localDateTime
                )
            }

        return ArrayList(events)
    }
}

class ListingsDeserializer: JsonDeserializer<ArrayList<Listing>> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ArrayList<Listing> {
        val listingsResponse = json?.asJsonArray ?: JsonArray()

        val listings = listingsResponse
            .map { listingJson ->
                // Get the listing data
                val listing = Gson().fromJson(listingJson, Listing::class.java)

                // Get the spot
                val row = listingJson.asJsonObject["row"].asString
                val section = listingJson.asJsonObject["zoneNo"]?.asLong ?: 0
                listing.copy(
                    spot = Spot(row, section)
                )
            }

        return ArrayList(listings)
    }
}