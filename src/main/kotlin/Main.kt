import api.gametime.BASE_URL
import api.gametime.GameTimeApi
import api.gametime.GameTimeService
import crawler.TickerCrawler
import emailer.EmailService

fun main() {
    val api = GameTimeApi.getRetrofit(BASE_URL)

    val gameTimeService = GameTimeService(api)

    val emailService = EmailService()

    val crawler = TickerCrawler(gameTimeService, emailService)

    crawler.findFlamesTickets()
}