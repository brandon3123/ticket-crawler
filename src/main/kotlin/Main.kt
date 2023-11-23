import api.gametime.GameTimeApi
import service.gametime.GameTimeService
import config.ExchangeRate
import config.GameTimeConfig
import config.loadConfig
import emailer.gametime.GameTimeEmailBuilder
import service.gametime.GameTimeTicketService
import crawler.TickerCrawler
import emailer.EmailService
import kotlin.concurrent.fixedRateTimer

private const val FIVE_MINUTES = 5L * 60L * 1000L
private const val TWICE_A_DAY = 12L * 60L * 60L * 1000L

fun main() {
    // Load config
    val config = loadConfig("config.yml")

    // get the exchange rate for USD -> CAD
    val exchangeRate = ExchangeRate(config.exchangeRate)

    // Spin up ticket services
    val gameTimeTicketService = gameTimeInit(config.gameTime, exchangeRate)

    // Email service
    val emailService = EmailService(config.email)

    val crawler = TickerCrawler(
        gameTimeTicketService = gameTimeTicketService,
        gameFilters = config.gameFilters,
        emailService = emailService
    )

    // Look for tickets every 5 minutes
    fixedRateTimer("ticket-check", period = TWICE_A_DAY) {
        crawler.findTickets()
    }
}

fun gameTimeInit(config: GameTimeConfig, exchangeRate: ExchangeRate): GameTimeTicketService {
    val gameTimeApi = GameTimeApi.getRetrofit(config, exchangeRate)
    val gameTimeService = GameTimeService(gameTimeApi)
    val gameTimeEmailBuilder = GameTimeEmailBuilder(config.buyUrl)

    return GameTimeTicketService(gameTimeService, gameTimeEmailBuilder)
}


