package com.example.currencyexchange.domain.usecase

import com.example.currencyexchange.data.repository.CurrencyRateRepository
import com.example.currencyexchange.domain.model.CurrencyRateModel
import javax.inject.Inject

class GetRelativeCurrencyRateUseCase @Inject constructor(
    private val currencyRateRepository: CurrencyRateRepository
) {
    suspend operator fun invoke(baseCode: String, targetCode: String): Float {
        val allEntries = currencyRateRepository.getDefaultCurrenciesRate()

        return if (allEntries.isSuccess()) {
            val baseRate = allEntries.successValue().find {
                it.nameCode == baseCode
            }!!.rate
            val targetRate = allEntries.successValue().find {
                it.nameCode == targetCode
            }!!.rate
            (1F / baseRate) * targetRate
        } else {
            1F
        }
    }
}