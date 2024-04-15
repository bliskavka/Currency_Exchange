package com.example.currencyexchange.di

import com.example.currencyexchange.data.mapper.ExchangeRateModelMapper
import com.example.currencyexchange.data.remote.ExchangeRateApi
import com.example.currencyexchange.data.remote.ExchangeRateDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideExchangeRateModelMapper() = ExchangeRateModelMapper()

    @Provides
    fun provideExchangeRateDataSource(exchangeRateApi: ExchangeRateApi, exchangeRateModelMapper: ExchangeRateModelMapper) =
        ExchangeRateDataSource(
            exchangeRateApi = exchangeRateApi,
            modelMapper = exchangeRateModelMapper
        )
}