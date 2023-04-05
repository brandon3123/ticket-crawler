package util

import api.gametime.BUY_URL
import model.gametime.Event
import model.gametime.Listing

private const val BORDER_STYLE = "border:1px solid black;"

fun List<Listing>.asHtmlTable(game: Event) =
    """    
    <table style="width:25%; text-align: center; $BORDER_STYLE">
            <tr style="$BORDER_STYLE">
            <th style="$BORDER_STYLE">Price</th>
            <th style="$BORDER_STYLE">Section</th>
            <th style="$BORDER_STYLE">Row</th>
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
            <td style="$BORDER_STYLE">$buyLink</td>
        </tr>"""
}

fun buyLink(href: String) = """<a href="$href"">Buy Tickets</a>"""
