import api.gametime.BASE_URL
import api.gametime.GameTimeApi
import api.gametime.GameTimeService
import crawler.TickerCrawler
import emailer.EmailService
import kotlin.concurrent.fixedRateTimer

private const val FIVE_MINUTES = 5L * 60L * 1000L

fun main() {
    val api = GameTimeApi.getRetrofit(BASE_URL)

    val gameTimeService = GameTimeService(api)

    val emailService = EmailService()

    val crawler = TickerCrawler(gameTimeService, emailService)

    // Look for tickets every 5 minutes
    fixedRateTimer("ticket-check", period = FIVE_MINUTES) {
        crawler.findTickets()
    }
}
