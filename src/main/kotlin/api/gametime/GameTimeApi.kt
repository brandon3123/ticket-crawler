package api.gametime

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import model.gametime.Event
import model.gametime.Listing
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.time.LocalDateTime

interface GameTimeApi {

    @GET("/v2/listings/{eventId}")
    suspend fun getListings(@Path("eventId") eventId: String): ArrayList<Listing>

    @GET("/v1/events$FLAMES_GAMES_PARAMS")
    suspend fun calgaryFlamesGames(): ArrayList<Event>

    companion object {
        private fun getClient(): OkHttpClient {
            return OkHttpClient.Builder().build()
        }

        fun getRetrofit(url: String): GameTimeApi {
            val eventType = object : TypeToken<ArrayList<Event>>() {}.type
            val listingType = object : TypeToken<ArrayList<Listing>>() {}.type

            val gson = GsonBuilder()
                .registerTypeAdapter(eventType, EventsDeserializer())
                .registerTypeAdapter(listingType, ListingsDeserializer())
                .create()

            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getClient())
                .build()
                .create(GameTimeApi::class.java)
        }
    }
}