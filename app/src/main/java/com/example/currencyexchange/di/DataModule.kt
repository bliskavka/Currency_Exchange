package com.example.currencyexchange.di

import com.example.currencyexchange.data.local.UserBalanceDao
import com.example.currencyexchange.data.mapper.ExchangeRateModelMapper
import com.example.currencyexchange.data.mapper.UserBalanceLocalDataSource
import com.example.currencyexchange.data.mapper.UserBalanceModelMapper
import com.example.currencyexchange.data.remote.ExchangeRateApi
import com.example.currencyexchange.data.remote.ExchangeRateRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideExchangeRateModelMapper() = ExchangeRateModelMapper()

    @Provides
    @Singleton
    fun provideExchangeRateDataSource(exchangeRateApi: ExchangeRateApi, exchangeRateModelMapper: ExchangeRateModelMapper) =
        ExchangeRateRemoteDataSource(
            exchangeRateApi = exchangeRateApi,
            modelMapper = exchangeRateModelMapper
        )

    @Provides
    fun provideUserBalanceModelMapper() = UserBalanceModelMapper()

    @Provides
    @Singleton
    fun provideUserBalanceLocalDataSource(userBalanceDao: UserBalanceDao, userBalanceModelMapper: UserBalanceModelMapper) =
        UserBalanceLocalDataSource(
            userBalanceDao = userBalanceDao,
            modelMapper = userBalanceModelMapper
        )
}