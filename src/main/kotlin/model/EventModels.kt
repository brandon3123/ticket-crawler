package model

import java.time.LocalDateTime

data class Event(
    val id: String,
    val name: String,
    val time: LocalDateTime,
    val team: NHLTeam,
    val vendor: Vendor,
    val awayTeam: Performer? = null
)

data class Performer(
    val shortName: String
)


enum class NHLTeam(val team: String) {

    ANA("Anaheim Ducks"),
    UTAH("Utah Hockey Club"),// maybe
    CGY("Calgary Flames"),
    CHI("Chicago Blackhawks"),
    COL("Colorado Avalanche"),
    DAL("Dallas Stars"),
    EDM("Edmonton Oilers"),
    LA("Los Angeles Kings"),
    MIN("Minnesota Wild"),
    NSH("Nashville Predators"),
    SEA("Seattle Kraken"),
    SJ("San Jose Sharks"),
    STL("St. Louis Blues"),
    VAN("Vancouver Canucks"),
    LAV("Vegas Golden Knights"),
    WPG("Winnipeg Jets"),
    BOS("Boston Bruins"),
    BUF("Buffalo Sabres"),
    CAR("Carolina Hurricanes"),
    CBJ("Columbus Blue Jackets"),
    DET("Detroit Red Wings"),
    FLA("Florida Panthers"),
    MTL("Montreal Canadiens"),
    NJ("New Jersey Devils"),
    NYI("New York Islanders"),
    NY("New York Rangers"),
    OTT("Ottawa Senators"),
    PHI("Philadelphia Flyers"),
    PIT("Pittsburgh Penguins"),
    TB("Tampa Bay Lightning"),
    TOR("Toronto Maple Leafs"),
    WAS("Washington Capitals"),
    WIN("Winnipeg Jets"),
    UNKNOWN("unknown");
}

private val lookup = NHLTeam.values().associateBy(NHLTeam::team)

fun String.opponent() = lookup[this]