package com.example.currencyexchange.domain.usecase

import com.example.currencyexchange.data.repository.CurrencyRateRepository
import com.example.currencyexchange.data.repository.UserBalanceRepository
import com.example.currencyexchange.ui.model.BalanceEntryUiModel
import com.example.currencyexchange.ui.viewmodel.HomeScreenState
import com.example.currencyexchange.util.CurrencyUtils
import com.example.currencyexchange.util.CurrencyUtils.getCurrencySymbol
import java.util.Currency
import javax.inject.Inject

class GetHomeScreenDataUseCase @Inject constructor(
    private val userBalanceRepository: UserBalanceRepository,
    private val currencyRateRepository: CurrencyRateRepository,
) {

    suspend operator fun invoke(): HomeScreenState {
        val allEntries = userBalanceRepository.getAllBalances()
        val baseEntry = userBalanceRepository.getBaseBalance()

        if (allEntries.isSuccess() && baseEntry.isSuccess()) {
            val entries = allEntries.successValue().map {
                BalanceEntryUiModel(
                    currencyCode = it.currencyCode,
                    currencySymbol = getCurrencySymbol(it.currencyCode),
                    balance = getCurrencySymbol(it.currencyCode) + it.amount.toString(),
                    baseBalance = getBaseBalance(baseEntry.successValue().currencyCode, it.currencyCode, it.amount)
                )
            }.toMutableList()
            entries.removeIf {
                it.currencyCode == baseEntry.successValue().currencyCode
            }

            return baseEntry.successValue().let {
                HomeScreenState(
                    balanceEntries = entries, baseBalance = BalanceEntryUiModel(
                        currencyCode = it.currencyCode,
                        currencySymbol = getCurrencySymbol(it.currencyCode),
                        balance = it.amount.toString(),
                        baseBalance = ""
                    )
                )
            }
        } else {
            return HomeScreenState(
                balanceEntries = listOf(),
                baseBalance = BalanceEntryUiModel(
                    currencyCode = "",
                    currencySymbol = ' ',
                    balance = "",
                    baseBalance = ""
                ),
                hasError = true,
                errorMessage = "Failed to get user balance" // TODO localize message
            )
        }
    }

    private suspend fun getBaseBalance(baseCurrencyCode: String, currencyCode: String, balance: Float): String {
        val result = currencyRateRepository.getSingleCurrencyRate(baseCurrencyCode, currencyCode)

        return if (result.isSuccess()) {
            val baseBalance = balance / result.successValue().rate
            getCurrencySymbol(baseCurrencyCode) + baseBalance.toString()
        } else {
            "n/a"
        }
    }
}