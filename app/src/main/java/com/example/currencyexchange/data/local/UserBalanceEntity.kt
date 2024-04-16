package com.example.currencyexchange.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_balances")
data class UserBalanceEntity(
    @PrimaryKey
    @ColumnInfo("currency_code") val currencyCode: String,
    @ColumnInfo(name = "balance_amount") val balanceAmount: Float,
) {
    override fun equals(other: Any?): Boolean {
        return other is UserBalanceEntity && other.currencyCode == this.currencyCode
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}