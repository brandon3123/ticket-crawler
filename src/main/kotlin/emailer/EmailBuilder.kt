package emailer

import model.Vendor
import model.Vendor.Companion.buyUrl
import model.generic.Event
import model.generic.GamesWithSeats
import model.generic.Listing
import java.time.format.DateTimeFormatter

class EmailBuilder{

    private val borderStyle = "border:1px solid black;"
    private val niceDateFormat = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' h:mm a")

    fun toEmailBody(data: List<GamesWithSeats>): String {
        val messageBuilder = StringBuilder()

        messageBuilder.append("<html><body>")

        messageBuilder.append("""
            <div>
                <h2>Game Time Results</h2>
            </div>
        """)

        data.forEach {
            it.data.forEach { (game, seats) ->
                val gameTimeString = niceDateFormat.format(game.time).toString()
                messageBuilder.append(
                    """
            <div>
                <h3>Seats for ${game.name} at $gameTimeString</h3>
            </div>
            ${seats.asHtmlTable(game, it.vendor)}
            </br>
            """
                )
            }
        }

        messageBuilder.append("</body></html>")

        return messageBuilder.toString()
    }

    private fun List<Listing>.asHtmlTable(game: Event, vendor: Vendor) =
        """    
    <table style="width:25%; text-align: center; $borderStyle">
            <tr style="$borderStyle">
            <th style="$borderStyle">Vendor</th>
            <th style="$borderStyle">Price</th>
            <th style="$borderStyle">Section</th>
            <th style="$borderStyle">Row</th>
            <th style="$borderStyle">Seats</th>
            <th style="$borderStyle">Purchase</th>
            </tr>
            ${joinToString("\n") { it.asRow(game, vendor) }}
            </table>
    """

    private fun Listing.asRow(game: Event, vendor: Vendor): String {
        val buyLink = buyLink(vendor.buyUrl(game.id, this.id))

        return """<tr style="$borderStyle">
            <td style="$borderStyle">${vendor.name}</td>
            <td style="$borderStyle">$${price}</td>
            <td style="$borderStyle">${spot.section}</td>
            <td style="$borderStyle">${spot.row}</td>
            <td style="$borderStyle">$numOfSeats</td>
            <td style="$borderStyle">$buyLink</td>
        </tr>"""
    }

    private fun buyLink(href: String) = """<a href="$href"">Buy Tickets</a>"""
}
