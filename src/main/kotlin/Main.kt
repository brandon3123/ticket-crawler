import api.fansfirst.FansFirstApi
import api.gametime.GameTimeApi
import service.gametime.GameTimeService
import config.ExchangeRate
import config.FansFirstConfig
import config.GameTimeConfig
import config.loadConfig
import emailer.gametime.GameTimeEmailBuilder
import service.gametime.GameTimeTicketService
import crawler.TickerCrawler
import emailer.EmailService
import emailer.fansfirst.FansFirstEmailBuilder
import model.TicketResults
import model.fansfirst.GamesWithSeats as FansFirstTickets
import model.TicketWorker
import model.generic.GamesWithSeats
import service.fansfirst.FansFirstService
import service.fansfirst.FansFirstTicketService
import kotlin.concurrent.fixedRateTimer

private const val FIVE_MINUTES = 5L * 60L * 1000L
private const val TWICE_A_DAY = 12L * 60L * 60L * 1000L
private const val SIX_HOURS = 6L * 60L * 60L * 1000L
private const val ONE_HOUR = 60L * 60L * 1000L

fun main() {
    // Load config
    val config = loadConfig("real.yml")

    // get the exchange rate for USD -> CAD
    val exchangeRate = ExchangeRate(config.exchangeRate)

    // Email service
    val emailService = EmailService(config.email)

    val gameTimeWorker = gameTimeWorker(config.gameTime, exchangeRate)
    val fansFirstWorker = fansFirstWorker(config.fansFirst)

    val workers = listOf(
        fansFirstWorker,
        gameTimeWorker
        // Add workers for another ticket site integration
    ) as List<TicketWorker<TicketResults>>

    val crawler = TickerCrawler(
        workers,
        gameFilters = config.gameFilters,
        emailService = emailService,
    )

    // Look for tickets every 5 minutes
    fixedRateTimer("ticket-check", period = ONE_HOUR) {
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

fun fansFirstWorker(config: FansFirstConfig): TicketWorker<FansFirstTickets> {
    val fansFirstApi = FansFirstApi.getRetrofit(config)
    val fansFirstService = FansFirstService(fansFirstApi)
    val fansFirstEmailBuilder = FansFirstEmailBuilder(config.buyUrl)
    val fansFirstTicketService = FansFirstTicketService(fansFirstService)

    return TicketWorker(fansFirstTicketService, fansFirstEmailBuilder)
}


