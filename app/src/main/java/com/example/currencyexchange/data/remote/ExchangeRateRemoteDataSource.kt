package com.example.currencyexchange.data.remote

import com.example.currencyexchange.core.Resource
import com.example.currencyexchange.data.mapper.ExchangeRateModelMapper
import com.example.currencyexchange.domain.model.CurrencyRateModel
import javax.inject.Inject

class ExchangeRateRemoteDataSource @Inject constructor(
    private val exchangeRateApi: ExchangeRateApi,
    private val modelMapper: ExchangeRateModelMapper,
) {

    private var eurExchangeRateCached = listOf<CurrencyRateModel>()
    private var eurExchangeRateCachedTime = 0L

    suspend fun getEurExchangeRate(): Resource<List<CurrencyRateModel>> {
        return try {
            if (isCacheExpired()) {
                val response = exchangeRateApi.getDefaultRate()
                val model = modelMapper.fromResponse(response)
                eurExchangeRateCached = model
                eurExchangeRateCachedTime = System.currentTimeMillis()
            }
            Resource.success(eurExchangeRateCached)
        } catch (e: Exception) {
            Resource.failure(e)
        }
    }

    private fun isCacheExpired() = System.currentTimeMillis() - eurExchangeRateCachedTime > 5000
}