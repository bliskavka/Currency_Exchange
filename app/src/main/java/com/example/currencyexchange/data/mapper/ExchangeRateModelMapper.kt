package com.example.currencyexchange.data.mapper

import com.example.currencyexchange.data.remote.DefaultRateResponse
import com.example.currencyexchange.domain.model.CurrencyRateModel

class ExchangeRateModelMapper {

    fun fromResponse(response: DefaultRateResponse) : List<CurrencyRateModel> {
        return response.rates.map {
            CurrencyRateModel(
                nameCode = it.key,
                baseNameCode = "EUR",
                rate = it.value
            )
        }
    }
}