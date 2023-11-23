package service.exchangerate

import api.exchangerate.ExchangeRateApi
import model.exchangerate.ExchangeRate

class ExchangeRateService (
    private val api: ExchangeRateApi,
    private val apiKey: String
) {

    suspend fun usdExchangeRate(): ExchangeRate {
        return api.getExchangeRate(apiKey, "USD")
    }
}
