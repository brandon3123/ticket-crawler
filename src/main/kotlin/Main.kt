import api.gametime.BASE_URL
import api.gametime.GameTimeApi
import api.gametime.GameTimeService
import crawler.TickerCrawler
import emailer.Emailer
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvBuilder
import kotlinx.coroutines.runBlocking

fun main() {
    val api = GameTimeApi.getRetrofit(BASE_URL)

    val gameTimeService = GameTimeService(api)

    val emailer = Emailer()

    val crawler = TickerCrawler(gameTimeService, emailer)

    crawler.findFlamesTickets()
}