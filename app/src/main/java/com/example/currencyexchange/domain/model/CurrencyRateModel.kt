package com.example.currencyexchange.domain.model

data class CurrencyRateModel(
    val nameCode: String,
    val baseNameCode: String,
    val rate: Float
)
