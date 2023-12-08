package api.fansfirst

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import config.FansFirstConfig
import model.fansfirst.Event
import model.fansfirst.Listing
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface FansFirstApi {

    @GET("/marketplace/listings/by/eventId/{eventId}")
    suspend fun getListings(@Path("eventId") eventId: String): ArrayList<Listing>

    @GET("/marketplace/events/homepage/by/homeTeamSlug/calgary-flames?includeMinPrices=true")
    suspend fun calgaryFlamesGames(): ArrayList<Event>

    companion object {
        private fun getClient(): OkHttpClient {
            return OkHttpClient.Builder().build()
        }

        fun getRetrofit(config: FansFirstConfig): FansFirstApi {
            val eventType = object : TypeToken<ArrayList<Event>>() {}.type
            val listingType = object : TypeToken<ArrayList<Listing>>() {}.type

            val gson = GsonBuilder()
                .registerTypeAdapter(eventType, EventsDeserializer())
                .registerTypeAdapter(listingType, ListingsDeserializer())
                .create()

            return Retrofit.Builder()
                .baseUrl(config.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getClient())
                .build()
                .create(FansFirstApi::class.java)
        }
    }
}