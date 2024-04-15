package com.example.currencyexchange.data.repository

import com.example.currencyexchange.domain.model.RateEntity

interface CurrencyRateRepository {

    fun getDefaultCurrenciesRate() : List<RateEntity>

    fun getRelativeCurrenciesRate(baseCurrency: String) : List<RateEntity>

    fun getSingleCurrencyRate(baseCurrency: String, targetCurrency: String) : RateEntity
}