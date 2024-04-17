package com.example.currencyexchange.util

import java.util.Currency

object CurrencyUtils {

    fun getCurrencySymbol(code: String) : Char {
        return Currency.getInstance(code).symbol.first()
    }
}