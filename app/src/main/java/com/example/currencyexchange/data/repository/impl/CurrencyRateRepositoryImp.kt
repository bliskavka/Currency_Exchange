package com.example.currencyexchange.data.repository.impl

import com.example.currencyexchange.core.Resource
import com.example.currencyexchange.data.remote.ExchangeRateRemoteDataSource
import com.example.currencyexchange.data.repository.CurrencyRateRepository
import com.example.currencyexchange.domain.model.RateEntity
import javax.inject.Inject

class CurrencyRateRepositoryImp @Inject constructor(
    private val exchangeRateRemoteDataSource: ExchangeRateRemoteDataSource
) : CurrencyRateRepository {
    override suspend fun getDefaultCurrenciesRate(): Resource<List<RateEntity>> {
        return exchangeRateRemoteDataSource.getEurExchangeRate()
    }

    override fun getRelativeCurrenciesRate(baseCurrency: String): Resource<List<RateEntity>>{
        TODO("Not yet implemented")
    }

    override fun getSingleCurrencyRate(baseCurrency: String, targetCurrency: String): Resource<RateEntity> {
        TODO("Not yet implemented")
    }
}