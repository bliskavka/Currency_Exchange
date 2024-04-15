package com.example.currencyexchange.data.repository.impl

import com.example.currencyexchange.data.repository.CurrencyRateRepository
import com.example.currencyexchange.domain.model.RateEntity
import javax.inject.Inject

class CurrencyRateRepositoryImp @Inject constructor() : CurrencyRateRepository {
    override fun getDefaultCurrenciesRate(): List<RateEntity> {
        TODO("Not yet implemented")
    }

    override fun getRelativeCurrenciesRate(baseCurrency: String): List<RateEntity> {
        TODO("Not yet implemented")
    }

    override fun getSingleCurrencyRate(baseCurrency: String, targetCurrency: String): RateEntity {
        TODO("Not yet implemented")
    }
}