package com.example.currencyexchange.data.repository.impl

import com.example.currencyexchange.core.Resource
import com.example.currencyexchange.data.remote.ExchangeRateRemoteDataSource
import com.example.currencyexchange.data.repository.CurrencyRateRepository
import com.example.currencyexchange.domain.model.CurrencyRateModel
import javax.inject.Inject

class CurrencyRateRepositoryImp @Inject constructor(
    private val exchangeRateRemoteDataSource: ExchangeRateRemoteDataSource
) : CurrencyRateRepository {
    override suspend fun getDefaultCurrenciesRate(): Resource<List<CurrencyRateModel>> {
        return exchangeRateRemoteDataSource.getEurExchangeRate()
    }

    override suspend fun getRelativeCurrenciesRate(baseCurrency: String): Resource<List<CurrencyRateModel>>{
        TODO("Not yet implemented")
    }

    override suspend fun getSingleCurrencyRate(baseCurrency: String, targetCurrency: String): Resource<CurrencyRateModel> {
        val baseRate = exchangeRateRemoteDataSource.getEurExchangeRateByCode(baseCurrency)
        val targetRate = exchangeRateRemoteDataSource.getEurExchangeRateByCode(targetCurrency)

        return if (baseRate.isSuccess() && targetRate.isSuccess()) {
            Resource.success(
                CurrencyRateModel(
                    rate = (1F / baseRate.successValue().rate) * targetRate.successValue().rate,
                    baseNameCode = baseCurrency,
                    nameCode = targetCurrency
                )
            )
        } else {
            baseRate
        }
    }
}