package model.generic

import java.time.LocalDateTime

data class Event(
    val id: String,
    val name: String,
    val time: LocalDateTime,
    val opponent: Opponent
)


enum class Opponent(val team: String) {

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
    STL("St.Louis Blues"),
    VAN("Vancouver Canucks"),
    VGK("Vegas Golden Knights"),
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
    NYR("New York Rangers"),
    OTT("Ottawa Senators"),
    PHI("Philadelphia Flyers"),
    PIT("Pittsburgh Penguins"),
    TB("Tamp Bay Lightning"),
    TOR("Toronto Maple Leafs"),
    WSH("Washington Capitals"),
    WIN("Winnipeg Jets"),
    UNKNOWN("unknown");
}

private val lookup = Opponent.values().associateBy(Opponent::team)

fun String.opponent() = lookup[this]