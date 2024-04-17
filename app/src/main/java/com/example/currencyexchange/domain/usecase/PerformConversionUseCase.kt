package com.example.currencyexchange.domain.usecase

import com.example.currencyexchange.data.repository.CurrencyRateRepository
import com.example.currencyexchange.data.repository.UserBalanceRepository
import com.example.currencyexchange.domain.model.UserBalanceModel
import com.example.currencyexchange.ui.viewmodel.ConvertScreenState
import javax.inject.Inject

class PerformConversionUseCase @Inject constructor(
    private val userBalanceRepository: UserBalanceRepository,
    private val getFeeValueUseCase: GetFeeValueUseCase,
    private val currencyRateRepository: CurrencyRateRepository
) {
    suspend operator fun invoke(state: ConvertScreenState): Boolean {
        val fromBalance = userBalanceRepository.getBalanceByCode(
            state.fromCurrencyList[state.fromSelectedItemId]
        )
        val toBalance = userBalanceRepository.getBalanceByCode(
            state.toCurrencyList[state.toSelectedItemId]
        )
        var baseBalance = userBalanceRepository.getBaseBalance()

        if (fromBalance.isFailure() || baseBalance.isFailure()) return false

        val fromAmountInBaseCurrency = if (fromBalance.successValue().currencyCode == baseBalance.successValue().currencyCode) {
            state.fromAmount
        } else {
            val rateRes = currencyRateRepository.getSingleCurrencyRate(
                baseBalance.successValue().currencyCode,
                fromBalance.successValue().currencyCode
            )
            if (rateRes.isFailure()) return false

            state.fromAmount / rateRes.successValue().rate
        }

        // To balance
        if (toBalance.isFailure()) {
            userBalanceRepository.createNewBalance(
                UserBalanceModel(
                    currencyCode = state.toCurrencyList[state.toSelectedItemId],
                    amount = state.toAmount
                )
            )
        } else {
            userBalanceRepository.changeBalanceAmount(
                UserBalanceModel(
                    currencyCode = toBalance.successValue().currencyCode,
                    amount = state.toAmount + toBalance.successValue().amount
                )
            )
        }

        // From balance
        userBalanceRepository.changeBalanceAmount(
            UserBalanceModel(
                currencyCode = fromBalance.successValue().currencyCode,
                amount = fromBalance.successValue().amount - state.fromAmount
            )
        )

        // Fee
        baseBalance = userBalanceRepository.getBaseBalance()
        userBalanceRepository.changeBalanceAmount(
            UserBalanceModel(
                currencyCode = baseBalance.successValue().currencyCode,
                amount = baseBalance.successValue().amount - getFeeValueUseCase(fromAmountInBaseCurrency).getValue(fromAmountInBaseCurrency)
            )
        )

        return true
    }
}