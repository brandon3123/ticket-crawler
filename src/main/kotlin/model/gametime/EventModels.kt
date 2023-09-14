package model.gametime

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Event(
    val id: String,
    val name: String,
    @Contextual
    val time: LocalDateTime
)
