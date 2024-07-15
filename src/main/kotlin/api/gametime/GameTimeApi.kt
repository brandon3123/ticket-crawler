package api.gametime

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import config.ExchangeRate
import config.GameTimeConfig
import model.generic.Event
import model.generic.Listing
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface GameTimeApi {

    @GET("/v2/listings/{eventId}?all_in_pricing=true")
    suspend fun getListings(@Path("eventId") eventId: String): ArrayList<Listing>

    @GET("/v1/events$FLAMES_GAMES_PARAMS")
    suspend fun calgaryFlamesGames(): ArrayList<Event>

    @GET("/v1/events$WRANGLERS_GAMES_PARAMS")
    suspend fun calgaryWranglersGames(): ArrayList<Event>

    companion object {
        private fun getClient(): OkHttpClient {
            return OkHttpClient.Builder().build()
        }

        fun getRetrofit(config: GameTimeConfig, exchangeRate: ExchangeRate): GameTimeApi {
            val eventType = object : TypeToken<ArrayList<Event>>() {}.type
            val listingType = object : TypeToken<ArrayList<Listing>>() {}.type

            val gson = GsonBuilder()
                .registerTypeAdapter(eventType, EventsDeserializer())
                .registerTypeAdapter(listingType, ListingsDeserializer(exchangeRate))
                .create()

            return Retrofit.Builder()
                .baseUrl(config.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getClient())
                .build()
                .create(GameTimeApi::class.java)
        }
    }
}