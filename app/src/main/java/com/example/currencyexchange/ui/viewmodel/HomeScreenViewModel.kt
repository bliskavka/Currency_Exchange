package com.example.currencyexchange.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.currencyexchange.ui.model.BalanceEntryUiModel
import com.example.currencyexchange.ui.viewmodel.HomeScreenEvent.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeScreenViewModel : ViewModel() {

    private val stateMutable = MutableStateFlow(getDefaultState())
    val state: StateFlow<HomeScreenState> = stateMutable

    private val actionMutable = MutableLiveData<HomeScreenAction>()
    val action: LiveData<HomeScreenAction> = actionMutable

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is OnScreenOpened -> {

            }

            is OnBuyButtonPressed -> {
            }

            is OnSellButtonPressed -> {
            }

            is OnBalanceEntryPressed -> {
            }

            is OnHistoryButtonPressed -> {
            }
        }
    }

    private fun getDefaultState() = HomeScreenState(
        balanceEntries = listOf(),
        baseBalance = BalanceEntryUiModel(
            currencyCode = "EUR",
            currencySymbol = '€',
            balance = "0.0",
            baseBalance = ""
        )
    )
}

class HomeScreenState(
    val balanceEntries: List<BalanceEntryUiModel>,
    val baseBalance: BalanceEntryUiModel,
)

sealed class HomeScreenEvent {
    data class OnScreenOpened(val isReopened: Boolean = false) : HomeScreenEvent()
    object OnBuyButtonPressed : HomeScreenEvent()
    object OnSellButtonPressed : HomeScreenEvent()
    object OnHistoryButtonPressed : HomeScreenEvent()
    data class OnBalanceEntryPressed(val entry: BalanceEntryUiModel) : HomeScreenEvent()
}

sealed class HomeScreenAction {
    object OpenSellDialog : HomeScreenAction()
    object OpenBuyDialog : HomeScreenAction()
    object OpenHistoryFragment : HomeScreenAction()
    data class ShowMessage(val text: String) : HomeScreenAction()
}
