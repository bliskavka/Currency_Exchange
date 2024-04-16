package com.example.currencyexchange.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.currencyexchange.ui.viewmodel.ConvertScreenAction.*
import com.example.currencyexchange.ui.viewmodel.ConvertScreenEvent.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ConvertViewModel : ViewModel() {

    private val stateMutable = MutableStateFlow(getDefaultState())
    val state: StateFlow<ConvertScreenState> = stateMutable

    private val actionMutable = MutableLiveData<ConvertScreenAction>()
    val action: LiveData<ConvertScreenAction> = actionMutable

    fun onEvent(event: ConvertScreenEvent) {
        when (event) {
            is OnBackButtonClicked -> actionMutable.value = CloseScreen
            is OnFromAmountEntered -> TODO()
            is OnFromCurrencyCodeClicked -> TODO()
            is OnScreenOpened -> TODO()
            is OnSubmitButtonClicked ->   actionMutable.value = CloseScreenWithResult(ConvertResult.SUBMITTED)
            is OnToAmountEntered -> TODO()
            is OnToCurrencyCodeClicked -> TODO()
        }
    }

    private fun getDefaultState() = ConvertScreenState(
        fromCurrencyList = listOf(),
        toCurrencyList = listOf(),
        fromCurrencyRate = "",
        toCurrencyRate = "",
        fromAmount = 0F,
        toAmount = 0F,
        feeBannerMessage = "",
        feeAmount = "",
    )
}

class ConvertScreenState(
    val fromCurrencyList: List<String>,
    val toCurrencyList: List<String>,
    val fromCurrencyRate: String,
    val toCurrencyRate: String,
    val fromAmount: Float,
    val toAmount: Float,
    val feeBannerMessage: String,
    val feeAmount: String
)

sealed class ConvertScreenEvent {
    data class OnScreenOpened(val isReopened: Boolean = false) : ConvertScreenEvent()
    object OnBackButtonClicked : ConvertScreenEvent()
    object OnSubmitButtonClicked : ConvertScreenEvent()
    object OnFromCurrencyCodeClicked : ConvertScreenEvent()
    object OnToCurrencyCodeClicked : ConvertScreenEvent()
    data class OnFromAmountEntered(val amount: Float) : ConvertScreenEvent()
    data class OnToAmountEntered(val amount: Float) : ConvertScreenEvent()
}

sealed class ConvertScreenAction {
    object CloseScreen : ConvertScreenAction()
    data class CloseScreenWithResult(val result: ConvertResult) : ConvertScreenAction()
    data class ShowMessage(val text: String) : ConvertScreenAction()
}

enum class ConvertResult {
    DISMISSED,
    SUBMITTED,
    DECLINED
}