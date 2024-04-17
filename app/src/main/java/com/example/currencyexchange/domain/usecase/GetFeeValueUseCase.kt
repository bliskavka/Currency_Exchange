package com.example.currencyexchange.domain.usecase

import com.example.currencyexchange.data.repository.CurrencyRateRepository
import com.example.currencyexchange.domain.model.FeeModel
import com.example.currencyexchange.domain.usecase.CommissionRule.*
import javax.inject.Inject

class GetFeeValueUseCase @Inject constructor(
    private val currencyRateRepository: CurrencyRateRepository
) {
    suspend operator fun invoke(amountInBaseCurrency: Float): FeeModel {
//        val commissionRule = commissionRepository.getActiveCommissionRule() TODO implement
//        val fee = commissionRepository.getFeeValue() TODO implement
//        val conversionCount = userHistoryRepository.getAllConversions().size TODO implement

        val commissionRule: CommissionRule = AfterConversionCount(5) // TODO hardcoded
        val fee = FeeModel(percentage = 0.7F) // TODO hardcoded
        val conversionCount = 10 // TODO hardcoded

        when (commissionRule) {
            is AfterConversionCount -> {
                if (conversionCount > commissionRule.limit) return fee
            }
            is AmountGreater -> return fee
            is AmountLower -> return fee
            is BeforeConversionCount -> return fee
            is FreeOnEachConversion -> return fee
            is PaidOnEachConversion -> return fee
        }
        return FeeModel.none()
    }
}

// TODO make it as separate model
sealed class CommissionRule {
    data class AfterConversionCount(val limit: Int) : CommissionRule() // Apply fee when conversions count reached limit
    data class BeforeConversionCount(val limit: Int) : CommissionRule() // Apply fee until conversions count reached limit
    data class FreeOnEachConversion(val each: Int) : CommissionRule() // Commission is not applied every N'th conversion
    data class PaidOnEachConversion(val each: Int) : CommissionRule() // Commission is applied every N'th conversion
    data class AmountGreater(val limit: Int) : CommissionRule() // If amount is greater then specified value then fee is applied
    data class AmountLower(val limit: Int) : CommissionRule() // If amount is lower then specified value then fee is applied
}