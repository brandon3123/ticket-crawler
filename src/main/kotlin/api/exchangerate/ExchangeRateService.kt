package api.exchangerate

import model.exchangerate.ExchangeRate

class ExchangeRateService (
    val api: ExchangeRateApi
) {

    suspend fun usdExchangeRate(): ExchangeRate {
        return api.getExchangeRate(currency = "USD")
    }
}
