package com.example.currencyexchange.di

import com.example.currencyexchange.data.repository.CurrencyRateRepository
import com.example.currencyexchange.data.repository.impl.CurrencyRateRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideCurrencyRateRepository(impl: CurrencyRateRepositoryImp) : CurrencyRateRepository
}