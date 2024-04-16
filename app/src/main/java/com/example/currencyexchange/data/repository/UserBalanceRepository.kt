package com.example.currencyexchange.data.repository

import com.example.currencyexchange.core.Resource
import com.example.currencyexchange.domain.model.UserBalanceEntity

interface UserBalanceRepository {

    suspend fun getAllBalances() : Resource<List<UserBalanceEntity>>

    suspend fun getBaseBalance() : Resource<UserBalanceEntity>

    suspend fun getBalanceByCode(currencyCode: String) : Resource<UserBalanceEntity>

    suspend fun createNewBalance(currencyCode: String, amount: Float)

    suspend fun changeBalanceAmount(currencyCode: String, amount: Float)

    suspend fun deleteBalance(currencyCode: String)
}