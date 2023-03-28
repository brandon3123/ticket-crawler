package api.gametime

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import model.gametime.Event
import model.gametime.Listing
import java.lang.reflect.Type

class EventsDeserializer : JsonDeserializer<ArrayList<Event>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ArrayList<Event> {
        val eventsResponseJson = json?.asJsonObject?.getAsJsonArray("events") ?: JsonArray()
        val events = eventsResponseJson
            .map { it.asJsonObject.get("event") }
            .map { Gson().fromJson(it, Event::class.java) }
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
        return ArrayList()
    }
}