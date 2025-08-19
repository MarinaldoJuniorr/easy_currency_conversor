package com.example.easy_currency_conversor

object ExchangeRates {

    private val eurPerUnit = mapOf(
        "EUR" to 1.0000,
        "BRL" to 6.3563,
        "CAD" to 1.6043,
        "CNY" to 8.3866,
        "DKK" to 7.4646,
        "NZD" to 1.9660,
        "PLN" to 4.2448,
        "USD" to 1.1670,
        "GBP" to 0.8668,
        "CHF" to 0.9409
    )

    fun convert(amount: Double, from: String, to: String): Double {
        val rFrom = eurPerUnit[from] ?: return Double.NaN
        val rTo = eurPerUnit[to] ?: return Double.NaN
        return amount * (rTo / rFrom)
    }
}






