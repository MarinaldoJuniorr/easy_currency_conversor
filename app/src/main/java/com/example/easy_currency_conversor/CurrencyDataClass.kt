package com.example.easy_currency_conversor

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class CurrencyItem (
    @DrawableRes
    val flagRes: Int,
    @StringRes
    val code: Int
)

