package com.example.currencyexchange.domain.model

data class RateEntity(
    val nameCode: String,
    val baseNameCode: String,
    val rate: Float
)
