package com.example.currencyexchange.ui.model

data class BalanceEntryUiModel(
    val currencyCode: String,
    val currencySymbol: Char,
    val balance: String,
    val baseBalance: String
)