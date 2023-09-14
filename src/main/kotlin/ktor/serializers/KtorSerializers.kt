package ktor.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializer(forClass = LocalDateTime::class)
object LocalDateTimeConverter: KSerializer<LocalDateTime> {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val dateString = decoder.decodeString()
        return LocalDateTime.parse(dateString, formatter)
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.toString())
    }
}
