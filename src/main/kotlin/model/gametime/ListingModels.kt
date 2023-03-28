package model.gametime

data class Listing(
    val price: Price,
    val row: String
)

data class Price(
    val total: Long
)