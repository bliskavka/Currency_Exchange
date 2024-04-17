package com.example.currencyexchange.domain.usecase

import com.example.currencyexchange.data.repository.CurrencyRateRepository
import javax.inject.Inject

class GetAvailableCurrenciesUseCase @Inject constructor(
    private val currencyRateRepository: CurrencyRateRepository
) {
    suspend operator fun invoke(): List<String> {
        val allEntries = currencyRateRepository.getDefaultCurrenciesRate()

        return if (allEntries.isSuccess()) {
            allEntries.successValue().map {
                it.nameCode
            }
        } else {
            listOf()
        }
    }
}