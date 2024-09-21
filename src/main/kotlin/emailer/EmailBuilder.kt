package emailer

import model.Vendor.Companion.buyUrl
import model.Ticket
import model.TicketId
import java.time.format.DateTimeFormatter

class EmailBuilder {

    private val niceDateFormat = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' h:mm a")

    fun toEmailBody(tickets: Map<TicketId, List<Ticket>>): String {
        val messageBuilder = StringBuilder()

        messageBuilder.append("<html><body>")

        tickets.forEach { (id, seats) ->
            val gameTimeString = niceDateFormat.format(id.time).toString()
            messageBuilder.append(
                """
            <div>
                <h2 style="${EmailStyle.headerStyle}">Seats for ${id.opponent.team} at ${id.host.team} at $gameTimeString</h3>
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
    <table style="${EmailStyle.tableStyle}">
            <tr style="${EmailStyle.thStyle}">
            <th style="${EmailStyle.tdThStyle}">Vendor</th>
            <th style="${EmailStyle.tdThStyle}">Price</th>
            <th style="${EmailStyle.tdThStyle}">Section</th>
            <th style="${EmailStyle.tdThStyle}">Row</th>
            <th style="${EmailStyle.tdThStyle}">Seats</th>
            <th style="${EmailStyle.tdThStyle}">Purchase</th>
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

        val trStyle = when (index % 2 == 0) {
            true -> "${EmailStyle.tdBorder} background-color: #f3f3f3;"
            else -> EmailStyle.tdBorder
        }

        return """<tr style="$trStyle">
            <td style="${EmailStyle.tdThStyle}">${v.value}</td>
            <td style="${EmailStyle.tdThStyle}">$${this.vendorListing.price}</td>
            <td style="${EmailStyle.tdThStyle}">${this.vendorListing.spot.section}</td>
            <td style="${EmailStyle.tdThStyle}">${this.vendorListing.spot.row}</td>
            <td style="${EmailStyle.tdThStyle}">${this.vendorListing.numOfSeats}</td>
            <td style="${EmailStyle.tdThStyle}">$buyLink</td>
        </tr>"""
    }

    private fun buyLink(href: String) = """<a href="$href" style="${EmailStyle.linkStyle}">Buy Tickets</a>"""
}
