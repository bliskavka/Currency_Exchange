package com.example.currencyexchange.data.repository

import com.example.currencyexchange.core.Resource
import com.example.currencyexchange.domain.model.RateEntity

interface CurrencyRateRepository {

    suspend fun getDefaultCurrenciesRate() : Resource<List<RateEntity>>

    suspend fun getRelativeCurrenciesRate(baseCurrency: String) : Resource<List<RateEntity>>

    suspend fun getSingleCurrencyRate(baseCurrency: String, targetCurrency: String) : Resource<RateEntity>
}