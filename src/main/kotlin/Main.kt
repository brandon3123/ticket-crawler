import api.gametime.GameTimeApi
import service.gametime.GameTimeService
import config.ExchangeRate
import config.GameTimeConfig
import config.loadConfig
import emailer.gametime.GameTimeEmailBuilder
import service.gametime.GameTimeTicketService
import crawler.TickerCrawler
import emailer.EmailService
import model.gametime.GamesWithSeats
import model.TicketWorker
import kotlin.concurrent.fixedRateTimer

private const val FIVE_MINUTES = 5L * 60L * 1000L
private const val TWICE_A_DAY = 12L * 60L * 60L * 1000L

fun main() {
    // Load config
    val config = loadConfig("config.yml")

    // get the exchange rate for USD -> CAD
    val exchangeRate = ExchangeRate(config.exchangeRate)

    // Email service
    val emailService = EmailService(config.email)

    val crawler = TickerCrawler(
        workers = listOf(
            gameTimeWorker(config.gameTime, exchangeRate)
            // Add workers for another ticket site integration
        ),
        gameFilters = config.gameFilters,
        emailService = emailService
    )

    // Look for tickets every 5 minutes
    fixedRateTimer("ticket-check", period = TWICE_A_DAY) {
        crawler.findTickets()
    }
}

fun gameTimeWorker(config: GameTimeConfig, exchangeRate: ExchangeRate): TicketWorker<GamesWithSeats> {
    val gameTimeApi = GameTimeApi.getRetrofit(config, exchangeRate)
    val gameTimeService = GameTimeService(gameTimeApi)
    val gameTimeEmailBuilder = GameTimeEmailBuilder(config.buyUrl)
    val gameTimeTicketService = GameTimeTicketService(gameTimeService)

    return TicketWorker(gameTimeTicketService, gameTimeEmailBuilder)
}


