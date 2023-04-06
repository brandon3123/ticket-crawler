package model.gametime

import java.time.LocalDateTime

data class Event(
    val id: String,
    val name: String,
    val time: LocalDateTime
)

