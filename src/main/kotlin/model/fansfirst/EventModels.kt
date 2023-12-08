package model.fansfirst

import java.time.LocalDateTime

data class Event(
    val id: String,
    val longName: String,
    val time: LocalDateTime,
    val opponent: String
)