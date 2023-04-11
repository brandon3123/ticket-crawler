package util

import api.gametime.BUY_URL
import model.gametime.Event
import model.gametime.GamesWithSeats
import model.gametime.Listing
import java.time.format.DateTimeFormatter

private const val BORDER_STYLE = "border:1px solid black;"
private val niceDateFormat = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' h:mm a")

fun List<Listing>.asHtmlTable(game: Event) =
    """    
    <table style="width:25%; text-align: center; $BORDER_STYLE">
            <tr style="$BORDER_STYLE">
            <th style="$BORDER_STYLE">Price</th>
            <th style="$BORDER_STYLE">Section</th>
            <th style="$BORDER_STYLE">Row</th>
            <th style="$BORDER_STYLE">Seats</th>
            <th style="$BORDER_STYLE">Purchase</th>
            </tr>
            ${joinToString("\n") { it.asRow(game) }}
            </table>
    """

fun Listing.asRow(game: Event): String {
    val buyLink = buyLink("${BUY_URL}/events/${game.id}/listings/$id")

    return """<tr style="$BORDER_STYLE">
            <td style="$BORDER_STYLE">$${price.total}</td>
            <td style="$BORDER_STYLE">${spot.section}</td>
            <td style="$BORDER_STYLE">${spot.row}</td>
            <td style="$BORDER_STYLE">$numOfSeats</td>
            <td style="$BORDER_STYLE">$buyLink</td>
        </tr>"""
}

fun GamesWithSeats.asHtmlEmail(): String {
    val messageBuilder = StringBuilder()

    messageBuilder.append("<html><body>")

    data.forEach { (game, seats) ->
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

fun buyLink(href: String) = """<a href="$href"">Buy Tickets</a>"""
