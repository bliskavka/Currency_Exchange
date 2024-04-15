package com.example.currencyexchange.data.mapper

import com.example.currencyexchange.data.remote.DefaultRateResponse
import com.example.currencyexchange.domain.model.RateEntity

class ExchangeRateModelMapper {

    fun fromResponse(response: DefaultRateResponse) : List<RateEntity> {
        return response.rates.map {
            RateEntity(
                nameCode = it.key,
                baseNameCode = "EUR",
                rate = it.value
            )
        }
    }
}