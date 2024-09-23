package service

import config.Sections
import model.Event
import model.Listing
import model.NHLTeam
import java.time.LocalDate

fun List<Listing>.sections(sections: Sections): List<Listing> {
    return filter { l ->
        val s = l.spot.section.toInt()
        val inLower = s >= sections.lower.from && s <= sections.lower.to
        val inUpper = s >= sections.upper.from && s <= sections.upper.to
        inLower || inUpper
    }
}

fun List<Event>.forDates(gameDays: List<LocalDate>) =
    filter {
        gameDays.contains(it.time.toLocalDate())
    }

fun List<Event>.vsing(opponents: List<NHLTeam>) =
    filter {
        opponents.contains(it.team)
    }

fun List<Listing>.under(price: Int) =
    filter {
        it.price.toLong() <= price
    }

fun List<Listing>.underRow(maxRow: Int) =
    filter {
        it.spot.row < maxRow
    }