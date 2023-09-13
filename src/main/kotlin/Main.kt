import api.exchangerate.ExchangeRateApi
import api.exchangerate.ExchangeRateService
import api.gametime.BASE_URL as GAME_TIME_URL
import api.exchangerate.BASE_URL as EXCHANGE_RATE_URL
import api.gametime.GameTimeApi
import api.gametime.GameTimeService
import crawler.TickerCrawler
import emailer.EmailService
import io.github.cdimascio.dotenv.Dotenv
import kotlinx.coroutines.runBlocking
import model.exchangerate.ExchangeRate
import kotlin.concurrent.fixedRateTimer

private const val FIVE_MINUTES = 5L * 60L * 1000L

fun main() {
    val gameTimeApi = GameTimeApi.getRetrofit(GAME_TIME_URL)

    val gameTimeService = GameTimeService(gameTimeApi)

    val emailService = EmailService()

    val crawler = TickerCrawler(gameTimeService, emailService)

    // Look for tickets every 5 minutes
    fixedRateTimer("ticket-check", period = FIVE_MINUTES) {
        crawler.findTickets()
    }
}



object Config {
    private val env = Dotenv.configure().load()

    object Email {
        val EMAIL: String = env["EMAIL"]
        val PASSWORD: String = env["PASSWORD"]
    }

    object ExchangeRate {
        val API_KEY: String = env["EXCHANGE_RATE_KEY"]
    }
}

object ExchangeRates {
    private val exchangeRates: ExchangeRate = getUsdExchangeRate()

    private fun getUsdExchangeRate(): ExchangeRate {
        val exchangeRateApi = ExchangeRateApi.getRetrofit(EXCHANGE_RATE_URL)
        val exchangeRateService = ExchangeRateService(exchangeRateApi)
        return runBlocking {
            exchangeRateService.usdExchangeRate()
        }
    }

    fun cad(): Double? = exchangeRates.conversionRates["CAD"]
}
