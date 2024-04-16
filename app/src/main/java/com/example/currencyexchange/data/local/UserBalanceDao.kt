package com.example.currencyexchange.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserBalanceDao {

    @Insert
    suspend fun insertBalance(balanceEntity: UserBalanceEntity)

    @Update
    suspend fun updateBalance(balanceEntity: UserBalanceEntity)

    @Delete
    suspend fun deleteBalance(balanceEntity: UserBalanceEntity)

    @Query("SELECT * FROM user_balances")
    suspend fun getAllEntities() : List<UserBalanceEntity>

    @Query("SELECT * FROM user_balances WHERE currency_code = :currencyCode")
    suspend fun getEntityByCode(currencyCode: String) : UserBalanceEntity
}