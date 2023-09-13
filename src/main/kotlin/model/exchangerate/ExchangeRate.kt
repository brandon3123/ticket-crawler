package model.exchangerate

import com.google.gson.annotations.SerializedName

data class ExchangeRate(
    @SerializedName("conversion_rates") val conversionRates: Map<String, Double>
)
