package com.example.currencyexchange.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyexchange.domain.usecase.GetHomeScreenDataUseCase
import com.example.currencyexchange.domain.usecase.GetInitialBalanceUseCase
import com.example.currencyexchange.ui.model.BalanceEntryUiModel
import com.example.currencyexchange.ui.viewmodel.HomeScreenEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    val userBalancesUseCase: GetHomeScreenDataUseCase,
    val getInitialBalanceUseCase: GetInitialBalanceUseCase
) : ViewModel() {

    private val stateMutable = MutableStateFlow(getDefaultState())
    val state: StateFlow<HomeScreenState> = stateMutable

    private val actionMutable = MutableLiveData<HomeScreenAction>()
    val action: LiveData<HomeScreenAction> = actionMutable

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is OnScreenOpened -> {
                fetchUserBalances()
            }

            is OnConvertButtonPressed -> {
                actionMutable.value = HomeScreenAction.OpenConvertDialog
            }

            is OnDepositButtonPressed -> {
                viewModelScope.launch {
                    val res = getInitialBalanceUseCase()
                    if (res) {
                        stateMutable.value = userBalancesUseCase()
                        actionMutable.value = HomeScreenAction.ShowMessage("10k EUR was kindly added to your account")
                    } else {
                        actionMutable.value = HomeScreenAction.ShowMessage("No more money :(")
                    }
                }
            }

            is OnBalanceEntryPressed -> {
            }

            is OnHistoryButtonPressed -> {
                actionMutable.value = HomeScreenAction.ShowMessage("Not yet implemented")
            }
        }
    }

    private fun fetchUserBalances() {
        viewModelScope.launch {
            stateMutable.value = userBalancesUseCase()
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
    val hasError: Boolean = false,
    val errorMessage: String = ""
)

sealed class HomeScreenEvent {
    data class OnScreenOpened(val isReopened: Boolean = false) : HomeScreenEvent()
    data object OnConvertButtonPressed : HomeScreenEvent()
    data object OnDepositButtonPressed : HomeScreenEvent()
    data object OnHistoryButtonPressed : HomeScreenEvent()
    data class OnBalanceEntryPressed(val entry: BalanceEntryUiModel) : HomeScreenEvent()
}

sealed class HomeScreenAction {
    data object OpenDepositDialog : HomeScreenAction()
    data object OpenConvertDialog : HomeScreenAction()
    data object OpenHistoryFragment : HomeScreenAction()
    data class ShowMessage(val text: String) : HomeScreenAction()
}
