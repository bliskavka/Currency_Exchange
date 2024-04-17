package com.example.currencyexchange.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_history")
class TransactionsHistoryEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo("from_currency_code") val fromCurrencyCode: String,
    @ColumnInfo("to_currency_code") val toCurrencyCode: String,
    @ColumnInfo(name = "from_amount") val fromAmount: Float,
    @ColumnInfo(name = "to_amount") val toAmount: Float,
)