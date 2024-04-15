package com.example.currencyexchange.data.remote

data class DefaultRateResponse (
    val base: String,
    val date: String,
    val rates: Map<String, Float>
)