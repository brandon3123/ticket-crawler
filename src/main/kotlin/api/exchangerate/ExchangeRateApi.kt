package api.exchangerate

import com.google.gson.GsonBuilder
import model.exchangerate.ExchangeRate
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {

    @GET("/v6/{apiKey}/latest/{currency}")
    suspend fun getExchangeRate(
        @Path("apiKey") apiKey: String,
        @Path("currency") currency: String
    ): ExchangeRate

    companion object {
        private fun getClient(): OkHttpClient {
            return OkHttpClient.Builder().build()
        }

        fun getRetrofit(url: String): ExchangeRateApi {
            val gson = GsonBuilder().create()

            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getClient())
                .build()
                .create(ExchangeRateApi::class.java)
        }
    }
}
