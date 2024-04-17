package com.example.currencyexchange.domain.usecase

import com.example.currencyexchange.data.repository.CurrencyRateRepository
import com.example.currencyexchange.domain.model.CurrencyRateModel
import javax.inject.Inject

class GetCurrencyRateByCodeUseCase @Inject constructor(
    private val currencyRateRepository: CurrencyRateRepository
) {
    suspend operator fun invoke(code: String): CurrencyRateModel {
        val allEntries = currencyRateRepository.getDefaultCurrenciesRate()

        return if (allEntries.isSuccess()) {
            allEntries.successValue().find {
                it.nameCode == code
            }!!
        } else {
            CurrencyRateModel("", "", 0F)
        }
    }
}