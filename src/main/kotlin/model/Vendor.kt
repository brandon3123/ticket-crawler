package model

enum class Vendor(val value: String) {
    GAME_TIME("Game Time"),
    FANS_FIRST("Fans First");

    companion object {

        fun Vendor.buyUrl(eventId: String, listingId: String): String {
            return when (this) {
                GAME_TIME -> "https://gametime.co/nhl-hockey/events/$eventId/listings/$listingId"
                FANS_FIRST -> "https://www.fansfirst.ca/games/$eventId/seats/$listingId"
            }
        }
    }
}