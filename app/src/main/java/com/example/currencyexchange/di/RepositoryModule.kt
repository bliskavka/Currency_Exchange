package com.example.currencyexchange.di

import com.example.currencyexchange.data.repository.CurrencyRateRepository
import com.example.currencyexchange.data.repository.UserBalanceRepository
import com.example.currencyexchange.data.repository.impl.CurrencyRateRepositoryImp
import com.example.currencyexchange.data.repository.impl.UserBalanceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideCurrencyRateRepository(impl: CurrencyRateRepositoryImp) : CurrencyRateRepository

    @Binds
    @Singleton
    abstract fun provideUserBalanceRepository(impl: UserBalanceRepositoryImpl) : UserBalanceRepository
}