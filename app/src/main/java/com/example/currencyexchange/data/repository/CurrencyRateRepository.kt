package com.example.currencyexchange.data.repository

import com.example.currencyexchange.core.Resource
import com.example.currencyexchange.domain.model.CurrencyRateModel

interface CurrencyRateRepository {

    suspend fun getDefaultCurrenciesRate() : Resource<List<CurrencyRateModel>>

    suspend fun getRelativeCurrenciesRate(baseCurrency: String) : Resource<List<CurrencyRateModel>>

    suspend fun getSingleCurrencyRate(baseCurrency: String, targetCurrency: String) : Resource<CurrencyRateModel>
}