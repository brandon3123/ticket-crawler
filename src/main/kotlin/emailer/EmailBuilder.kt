package emailer

import model.Vendor.Companion.buyUrl
import model.Ticket
import model.TicketId
import java.time.format.DateTimeFormatter

class EmailBuilder {

    private val borderStyle = "border:1px solid black;"
    private val niceDateFormat = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' h:mm a")

    fun toEmailBody(tickets: Map<TicketId, List<Ticket>>): String {
        val messageBuilder = StringBuilder()

        messageBuilder.append("<html><body>")

        messageBuilder.append(
            """
            <div>
                <h2>Game Time Results</h2>
            </div>
        """
        )

        tickets.forEach { (id, seats) ->
            val gameTimeString = niceDateFormat.format(id.time).toString()
            messageBuilder.append(
                """
            <div>
                <h3>Seats for ${id.opponent.team} at ${id.host.team} at $gameTimeString</h3>
            </div>
            ${seats.asHtmlTable()}
            </br>
            """
            )
        }

        messageBuilder.append("</body></html>")

        return messageBuilder.toString()
    }

    private fun List<Ticket>.asHtmlTable() =
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
            ${joinToString("\n") { it.asRow() }}
            </table>
    """

    private fun Ticket.asRow(): String {
        val v = this.vendorListing.vendor
        val buyLink = buyLink(v.buyUrl(this.vendorEvent.id, this.vendorListing.id))

        return """<tr style="$borderStyle">
            <td style="$borderStyle">${v.name}</td>
            <td style="$borderStyle">$${this.vendorListing.price}</td>
            <td style="$borderStyle">${this.vendorListing.spot.section}</td>
            <td style="$borderStyle">${this.vendorListing.spot.row}</td>
            <td style="$borderStyle">${this.vendorListing.numOfSeats}</td>
            <td style="$borderStyle">$buyLink</td>
        </tr>"""
    }

    private fun buyLink(href: String) = """<a href="$href"">Buy Tickets</a>"""
}
