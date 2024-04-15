package com.example.currencyexchange.data.remote

import retrofit2.http.GET

interface ExchangeRateApi {

    @GET("/tasks/api/currency-exchange-rates")
    suspend fun getDefaultRate() : DefaultRateResponse
}