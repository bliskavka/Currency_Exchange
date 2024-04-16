package com.example.currencyexchange.data.remote

import com.example.currencyexchange.core.Resource
import com.example.currencyexchange.data.mapper.ExchangeRateModelMapper
import com.example.currencyexchange.domain.model.RateEntity
import javax.inject.Inject

class ExchangeRateRemoteDataSource @Inject constructor(
    private val exchangeRateApi: ExchangeRateApi,
    private val modelMapper: ExchangeRateModelMapper,
) {
    suspend fun getEurExchangeRate(): Resource<List<RateEntity>> {
        return try {
            val response = exchangeRateApi.getDefaultRate()
            val model = modelMapper.fromResponse(response)
            Resource.success(model)
        } catch (e: Exception) {
            Resource.failure(e)
        }
    }

    suspend fun getEurExchangeRateByCode(currencyCode: String): Resource<RateEntity> {
        return try {
            val response = exchangeRateApi.getDefaultRate() // TODO replace with dedicated endpoint
            val model = modelMapper.fromResponse(response)
            Resource.success(model.find { it.nameCode == currencyCode }!!)
        } catch (e: Exception) {
            Resource.failure(e)
        }
    }
}