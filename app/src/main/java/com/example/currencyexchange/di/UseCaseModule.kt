package com.example.currencyexchange.di

import com.example.currencyexchange.data.repository.CurrencyRateRepository
import com.example.currencyexchange.data.repository.UserBalanceRepository
import com.example.currencyexchange.domain.usecase.GetHomeScreenDataUseCase
import com.example.currencyexchange.ui.viewmodel.HomeScreenViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetHomeScreenDataUseCase(
        userBalanceRepository: UserBalanceRepository,
        currencyRateRepository: CurrencyRateRepository
    ) = GetHomeScreenDataUseCase(userBalanceRepository, currencyRateRepository)
}