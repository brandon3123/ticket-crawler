package emailer

import model.Vendor.Companion.buyUrl
import model.Ticket
import model.TicketId
import java.time.format.DateTimeFormatter

class EmailBuilder {

    private val tableStyle = """
            border-collapse: collapse;
            margin: 25px 0;
            font-family: sans-serif;
            min-width: 400px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
    """.trimIndent()

    private val thStyle = """
        background-color: #009879;
        color: #ffffff;
        text-align: left;
        border-bottom: 1px solid #dddddd;
        """

    private val tdThStyle = "padding: 12px 15px;"
    private val tdBorder = "border-bottom: 1px solid #dddddd;"

    private val linkStyle = """
            text-decoration: none;
            padding: 5px 5px;
            border-radius: 8px;
            font-family: Monospace;
            text-align: center;
            color: white;
            background-color: #009879;
    """.trimIndent()

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
    <table style="$tableStyle">
            <tr style="$thStyle">
            <th style="$tdThStyle">Vendor</th>
            <th style="$tdThStyle">Price</th>
            <th style="$tdThStyle">Section</th>
            <th style="$tdThStyle">Row</th>
            <th style="$tdThStyle">Seats</th>
            <th style="$tdThStyle">Purchase</th>
            </tr>
            ${
                mapIndexed { index, ticket ->
                    ticket.asRow(index)    
                }.joinToString("\n")
                
            }
            </table>
    """

    private fun Ticket.asRow(index: Int): String {
        val v = this.vendorListing.vendor
        val buyLink = buyLink(v.buyUrl(this.vendorEvent.id, this.vendorListing.id))

        val isEvenRow = index % 2 == 0

        val trStyle = if (isEvenRow) "$tdBorder background-color: #f3f3f3;"  else tdBorder

        return """<tr style="$trStyle">
            <td style="$tdThStyle">${v.value}</td>
            <td style="$tdThStyle">$${this.vendorListing.price}</td>
            <td style="$tdThStyle">${this.vendorListing.spot.section}</td>
            <td style="$tdThStyle">${this.vendorListing.spot.row}</td>
            <td style="$tdThStyle">${this.vendorListing.numOfSeats}</td>
            <td style="$tdThStyle">$buyLink</td>
        </tr>"""
    }

    private fun buyLink(href: String) = """<a href="$href" style="$linkStyle">Buy Tickets</a>"""
}
