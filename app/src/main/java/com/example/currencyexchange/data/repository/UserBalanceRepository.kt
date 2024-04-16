package com.example.currencyexchange.data.repository

import com.example.currencyexchange.core.Resource
import com.example.currencyexchange.domain.model.UserBalanceModel

interface UserBalanceRepository {

    suspend fun getAllBalances() : Resource<List<UserBalanceModel>>

    suspend fun getBaseBalance() : Resource<UserBalanceModel>

    suspend fun getBalanceByCode(currencyCode: String) : Resource<UserBalanceModel>

    suspend fun createNewBalance(userBalanceModel: UserBalanceModel)

    suspend fun changeBalanceAmount(userBalanceModel: UserBalanceModel)

    suspend fun deleteBalance(userBalanceModel: UserBalanceModel)
}