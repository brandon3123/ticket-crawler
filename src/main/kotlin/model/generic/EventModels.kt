package model.generic

import java.time.LocalDateTime

data class Event(
    val id: String,
    val name: String,
    val time: LocalDateTime,
    val opponent: Opponent
)



enum class Opponent(val team: String) {



    ANA("asd"), ARI("asd"), CGY("asd"), CHI("asd"), COL("asd"), DAL("asd"), EDM("Edmonton Oilers"), LA("asd"), MIN("asd"), NSH("asd"), SEA(
    "asd"
    ),
    SJ("asd"), STL("asd"), VAN("asd"), VGK("asd"), WPG("asd"), BOS("asd"), BUF("asd"), CAR("asd"), CBJ("asd"), DET("asd"), FLA(
    "asd"
    ),
    MTL("asd"), NJ("asd"), NYI("asd"), NYR("asd"), OTT("asd"), PHI("asd"), PIT("asd"), TB("asd"), TOR("asd"), WSH("asd"), UNKNOWN(
    "asd"
    ), WIN("asd")
}



private val lookup = Opponent.values().associateBy(Opponent::team)

fun String.opponent() = lookup[this]