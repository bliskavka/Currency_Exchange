package com.example.currencyexchange.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyexchange.domain.usecase.GetAvailableCurrenciesUseCase
import com.example.currencyexchange.domain.usecase.GetCurrencyRateByCodeUseCase
import com.example.currencyexchange.domain.usecase.GetFeeValueUseCase
import com.example.currencyexchange.domain.usecase.GetRelativeCurrencyRateUseCase
import com.example.currencyexchange.domain.usecase.GetUserBalancesCodesUseCase
import com.example.currencyexchange.domain.usecase.PerformConversionUseCase
import com.example.currencyexchange.domain.usecase.ValidateConversionUseCase
import com.example.currencyexchange.ui.viewmodel.ConvertScreenAction.*
import com.example.currencyexchange.ui.viewmodel.ConvertScreenEvent.*
import com.example.currencyexchange.util.CurrencyUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConvertViewModel @Inject constructor(
    val getUserBalancesCodesUseCase: GetUserBalancesCodesUseCase,
    val getAvailableCurrenciesUseCase: GetAvailableCurrenciesUseCase,
    val getCurrencyRateByCodeUseCase: GetCurrencyRateByCodeUseCase,
    val getRelativeCurrencyRateUseCase: GetRelativeCurrencyRateUseCase,
    val validateConversionUseCase: ValidateConversionUseCase,
    val performConversionUseCase: PerformConversionUseCase,
    val getFeeValueUseCase: GetFeeValueUseCase
) : ViewModel() {

    private val stateMutable = MutableStateFlow(getDefaultState())
    val state: StateFlow<ConvertScreenState> = stateMutable

    private val actionMutable = MutableLiveData<ConvertScreenAction>()
    val action: LiveData<ConvertScreenAction> = actionMutable

    fun onEvent(event: ConvertScreenEvent) {
        when (event) {
            is OnBackButtonClicked -> actionMutable.value = CloseScreen
            is OnFromAmountEntered ->
                viewModelScope.launch {
                    stateMutable.update {
                        val fromAmount = validateAmount(event.amount)
                        it.copy(
                            fromAmount = fromAmount,
                            toAmount = calculateToAmount(fromAmount, it.fromSelectedItemId, it.toSelectedItemId)
                        )
                    }
                }

            is OnToAmountEntered -> {
                viewModelScope.launch {
                    stateMutable.update {
                        val toAmount = validateAmount(event.amount)
                        val fromAmount = toAmount / getRelativeCurrencyRateUseCase(
                            it.fromCurrencyList[it.fromSelectedItemId],
                            it.toCurrencyList[it.toSelectedItemId]
                        )
                        it.copy(
                            fromAmount = fromAmount,
                            toAmount = toAmount
                        )
                    }
                }
            }

            is OnFromCurrencyCodeSelected -> {
                viewModelScope.launch {
                    stateMutable.update {
                        val from = getCurrencyRateByCodeUseCase(it.fromCurrencyList[event.id])
                        it.copy(
                            fromCurrencyRate = CurrencyUtils.getCurrencySymbol(from.baseNameCode) + "${it.fromAmount / from.rate}",
                            fromSelectedItemId = event.id,
                            toAmount = calculateToAmount(it.fromAmount, event.id, it.toSelectedItemId)
                        )
                    }
                }
            }

            is OnToCurrencyCodeSelected -> {
                viewModelScope.launch {
                    stateMutable.update {
                        val to = getCurrencyRateByCodeUseCase(it.toCurrencyList[event.id])
                        it.copy(
                            toCurrencyRate = CurrencyUtils.getCurrencySymbol(to.baseNameCode) + "${it.toAmount / to.rate}",
                            toSelectedItemId = event.id,
                            toAmount = calculateToAmount(it.fromAmount, it.fromSelectedItemId, event.id)
                        )
                    }
                }
            }

            is OnScreenOpened -> {
                viewModelScope.launch {
                    stateMutable.update {
                        it.copy(
                            fromCurrencyList = getUserBalancesCodesUseCase(),
                            toCurrencyList = getAvailableCurrenciesUseCase(),
                            feeAmount = prepareFeeString(0F)
                        )
                    }
                }
            }

            is OnSubmitButtonClicked -> {
                viewModelScope.launch {
                    val result = validateConversionUseCase(state.value)
                    if (result.isValid) {
                        performConversionUseCase(state.value)
                        actionMutable.value = CloseScreenWithResult(ConvertResult.SUBMITTED)
                    } else {
                        actionMutable.value = ShowMessage(result.errorMessage)
                    }
                }
            }
        }
    }

    private suspend fun calculateToAmount(fromAmount: Float, fromSelectedItemId: Int, toSelectedItemId: Int) = state.value.let {
        fromAmount * getRelativeCurrencyRateUseCase(
            it.fromCurrencyList[fromSelectedItemId],
            it.toCurrencyList[toSelectedItemId]
        )
    }

    private suspend fun prepareFeeString(amount: Float) : String {
        val fee = getFeeValueUseCase(amount)
        return if (fee.percentage != null && fee.percentage > 0) {
            fee.percentage.toString() + "%"
        } else if (fee.fixedValue != null && fee.fixedValue > 0){
            fee.fixedValue.toString() + "€" // TODO get base currency symbol
        } else {
            ""
        }
    }

    private fun validateAmount(amount: String): Float {
        if (amount.isNullOrEmpty()) return 0F
        return amount.toFloat()
    }

    private fun getDefaultState() = ConvertScreenState(
        fromCurrencyList = listOf(),
        toCurrencyList = listOf(),
        fromSelectedItemId = 0,
        toSelectedItemId = 0,
        fromCurrencyRate = "",
        toCurrencyRate = "",
        fromAmount = 0F,
        toAmount = 0F,
        feeBannerMessage = "",
        feeAmount = "",
    )
}

data class ConvertScreenState(
    val fromCurrencyList: List<String>,
    val toCurrencyList: List<String>,
    val fromSelectedItemId: Int,
    val toSelectedItemId: Int,
    val fromCurrencyRate: String,
    val toCurrencyRate: String,
    val fromAmount: Float,
    val toAmount: Float,
    val feeBannerMessage: String,
    val feeAmount: String,
)

sealed class ConvertScreenEvent {
    data class OnScreenOpened(val isReopened: Boolean = false) : ConvertScreenEvent()
    data object OnBackButtonClicked : ConvertScreenEvent()
    data object OnSubmitButtonClicked : ConvertScreenEvent()
    data class OnFromCurrencyCodeSelected(val id: Int) : ConvertScreenEvent()
    data class OnToCurrencyCodeSelected(val id: Int) : ConvertScreenEvent()
    data class OnFromAmountEntered(val amount: String) : ConvertScreenEvent()
    data class OnToAmountEntered(val amount: String) : ConvertScreenEvent()
}

sealed class ConvertScreenAction {
    data object CloseScreen : ConvertScreenAction()
    data class CloseScreenWithResult(val result: ConvertResult) : ConvertScreenAction()
    data class ShowMessage(val text: String) : ConvertScreenAction()
}

enum class ConvertResult {
    DISMISSED,
    SUBMITTED,
    DECLINED
}