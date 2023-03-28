import api.gametime.BASE_URL
import api.gametime.GameTimeApi
import api.gametime.GameTimeService
import kotlinx.coroutines.runBlocking

fun main() {
    val api = GameTimeApi.getRetrofit(BASE_URL)

    val gameTimeService = GameTimeService(api)

    runBlocking {
        val events = gameTimeService.calgaryFlamesGames()
        val listings = gameTimeService.seats()
        println(events)
        println(listings)
    }
}