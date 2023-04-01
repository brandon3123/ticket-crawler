package model.gametime

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Event(
    val id: String,
    val name: String,
    val time : LocalDateTime
)

