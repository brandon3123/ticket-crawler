package config

import api.exchangerate.ExchangeRateApi
import api.exchangerate.ExchangeRateService
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.runBlocking
import model.exchangerate.ExchangeRate
import java.io.FileNotFoundException
import java.time.LocalDate

private val mapper = ObjectMapper(YAMLFactory())
    .registerKotlinModule()
    .registerModule(JavaTimeModule())

fun loadConfig(configPath: String): Config {
    val resource = Config::class.java.classLoader.getResource(configPath)
        ?: throw FileNotFoundException("Unable to read config file '$configPath'")

    return mapper.readValue(resource.readText())
}

data class Config(
    val email: EmailConfig,
    val exchangeRate: ExchangeRateConfig,
    val gameFilters: GameFilters,
    val gameTime: GameTimeConfig,
    val seatGeek: SeatGeekConfig
)

data class ExchangeRate(
    val config: ExchangeRateConfig
) {
    private val exchangeRates: ExchangeRate = getUsdExchangeRate()

    private fun getUsdExchangeRate(): ExchangeRate {
        val exchangeRateApi = ExchangeRateApi.getRetrofit(config.baseUrl)
        val exchangeRateService = ExchangeRateService(exchangeRateApi, config.apiKey)
        return runBlocking {
            exchangeRateService.usdExchangeRate()
        }
    }

    fun cad(): Double? = exchangeRates.conversionRates["CAD"]
}

data class GameTimeConfig(
    val baseUrl: String,
    val buyUrl: String
)

data class SeatGeekConfig(
    val secret: String,
    val clientId: String
)

data class ExchangeRateConfig(
    val baseUrl: String,
    val apiKey: String
)

data class EmailConfig(
    val email: String,
    val password: String,
    val recipients: List<String>
)

data class GameFilters(
    val maxPrice: Int,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val days: List<LocalDate>?
)
