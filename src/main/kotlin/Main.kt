import api.exchangerate.ExchangeRateApi
import api.exchangerate.ExchangeRateService
import api.gametime.BASE_URL as GAME_TIME_URL
import api.exchangerate.BASE_URL as EXCHANGE_RATE_URL
import api.gametime.GameTimeApi
import api.gametime.GameTimeService
import crawler.TickerCrawler
import emailer.EmailService
import io.github.cdimascio.dotenv.Dotenv
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.modules.SerializersModule
import ktor.serializers.LocalDateTimeConverter
import model.exchangerate.ExchangeRate
import routes.games
import java.time.LocalDateTime

private const val FIVE_MINUTES = 5L * 60L * 1000L

fun main() {
    val gameTimeApi = GameTimeApi.getRetrofit(GAME_TIME_URL)

    val gameTimeService = GameTimeService(gameTimeApi)

    val emailService = EmailService()

    val crawler = TickerCrawler(gameTimeService, emailService)

    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json(
                json = kotlinx.serialization.json.Json {
                    serializersModule = SerializersModule {
                        contextual(LocalDateTime::class, LocalDateTimeConverter)
                    }
                }
            )
        }

        routing {
            games(gameTimeService)
            // Comment for Bronson step 2
            // Add your seats route here, start the app. You'll probably get an error about json or something
            // when you start the app and hit the GET endpoint. Proceed to step 3, and try again after thats completed.
        }
    }.start()

//    // Look for tickets every 5 minutes
//    fixedRateTimer("ticket-check", period = FIVE_MINUTES) {
//        crawler.findTickets()
//    }



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
