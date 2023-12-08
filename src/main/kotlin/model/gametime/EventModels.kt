package model.gametime

import java.time.LocalDateTime

data class Event(
    val id: String,
    val name: String,
    val time: LocalDateTime,
    val opponent: Opponent
)

enum class Opponent {
    ANA, ARI, CGY, CHI, COL, DAL, EDM, LA, MIN, NSH, SEA,
    SJ, STL, VAN, VGK, WPG, BOS, BUF, CAR, CBJ, DET, FLA,
    MTL, NJ, NYI, NYR, OTT, PHI, PIT, TB, TOR, WSH, UNKNOWN
}

