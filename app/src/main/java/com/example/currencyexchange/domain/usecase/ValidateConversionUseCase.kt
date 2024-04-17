package com.example.currencyexchange.domain.usecase

import com.example.currencyexchange.data.repository.CurrencyRateRepository
import com.example.currencyexchange.data.repository.UserBalanceRepository
import com.example.currencyexchange.ui.viewmodel.ConvertScreenState
import javax.inject.Inject

class ValidateConversionUseCase @Inject constructor(
    private val currencyRateRepository: CurrencyRateRepository,
    private val userBalanceRepository: UserBalanceRepository,
    private val getFeeValueUseCase: GetFeeValueUseCase
) {
    suspend operator fun invoke(state: ConvertScreenState): ValidationResult {
        val rate = currencyRateRepository.getSingleCurrencyRate(
            state.fromCurrencyList[state.fromSelectedItemId],
            state.toCurrencyList[state.toSelectedItemId]
        )

        val balance = userBalanceRepository.getBalanceByCode(
            state.fromCurrencyList[state.fromSelectedItemId]
        )

        val baseBalance = userBalanceRepository.getBaseBalance()

        if (rate.isFailure() || balance.isFailure() || baseBalance.isFailure())
            return ValidationResult(false, "Network Error") //TODO use error mapper

        if (state.fromAmount * rate.successValue().rate != state.toAmount)
            return ValidationResult(false, "Rate is not up-to-date")

        if (state.fromAmount <= 0F)
            return ValidationResult(false, "Amount can't be zero or negative")

        if (state.fromAmount > balance.successValue().amount)
            return ValidationResult(false, "Not enough money to convert")

        val feeChargeBalanceAmount = if (balance.successValue().currencyCode == baseBalance.successValue().currencyCode) {
            baseBalance.successValue().amount - state.fromAmount
        } else {
            baseBalance.successValue().amount
        }
        val fee = getFeeValueUseCase(feeChargeBalanceAmount).getValue(feeChargeBalanceAmount)
        if (feeChargeBalanceAmount < fee)
            return ValidationResult(false, "Not enough money to pay a fee")

        return ValidationResult(true)
    }
}

data class ValidationResult (
    val isValid: Boolean,
    val errorMessage: String = ""
)