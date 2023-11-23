package crawler

import model.gametime.Event
import model.gametime.GamesWithSeats
import model.gametime.Listing
import java.time.format.DateTimeFormatter

class GameTimeEmailBuilder(
    private val buyUrl: String
) : EmailBuilder<GamesWithSeats> {

    private val borderStyle = "border:1px solid black;"
    private val niceDateFormat = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' h:mm a")

    override fun toEmailBody(data: GamesWithSeats): String {
        val messageBuilder = StringBuilder()

        messageBuilder.append("<html><body>")

        data.data.forEach { (game, seats) ->
            val gameTimeString = niceDateFormat.format(game.time).toString()
            messageBuilder.append(
                """
            <div>
                <h3>Seats for ${game.name} at $gameTimeString</h3>
            </div>
            ${seats.asHtmlTable(game)}
            </br>
            """
            )
        }

        messageBuilder.append("</body></html>")

        return messageBuilder.toString()
    }

    private fun List<Listing>.asHtmlTable(game: Event) =
        """    
    <table style="width:25%; text-align: center; $borderStyle">
            <tr style="$borderStyle">
            <th style="$borderStyle">Price</th>
            <th style="$borderStyle">Section</th>
            <th style="$borderStyle">Row</th>
            <th style="$borderStyle">Seats</th>
            <th style="$borderStyle">Purchase</th>
            </tr>
            ${joinToString("\n") { it.asRow(game) }}
            </table>
    """

    private fun Listing.asRow(game: Event): String {
        val buyLink = buyLink("${buyUrl}/events/${game.id}/listings/$id")

        return """<tr style="$borderStyle">
            <td style="$borderStyle">$${price.total}</td>
            <td style="$borderStyle">${spot.section}</td>
            <td style="$borderStyle">${spot.row}</td>
            <td style="$borderStyle">$numOfSeats</td>
            <td style="$borderStyle">$buyLink</td>
        </tr>"""
    }

    private fun buyLink(href: String) = """<a href="$href"">Buy Tickets</a>"""
}
