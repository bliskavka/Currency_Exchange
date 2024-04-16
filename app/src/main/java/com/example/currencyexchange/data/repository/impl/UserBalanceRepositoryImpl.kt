package com.example.currencyexchange.data.repository.impl

import com.example.currencyexchange.core.Resource
import com.example.currencyexchange.data.repository.UserBalanceRepository
import com.example.currencyexchange.domain.model.UserBalanceEntity
import javax.inject.Inject

class UserBalanceRepositoryImpl @Inject constructor(): UserBalanceRepository {
    override suspend fun getAllBalances(): Resource<List<UserBalanceEntity>> {
        return Resource.success(listOf(
            UserBalanceEntity("GBP", 2300.12F),
            UserBalanceEntity("EUR", 10000F),
            UserBalanceEntity("USD", 17.3F),
            UserBalanceEntity("UAH", 33760F),
        ))
    }

    override suspend fun getBaseBalance(): Resource<UserBalanceEntity> {
        return Resource.success(UserBalanceEntity("EUR", 10000F))
    }

    override suspend fun getBalanceByCode(currencyCode: String): Resource<UserBalanceEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun createNewBalance(currencyCode: String, amount: Float) {
        TODO("Not yet implemented")
    }

    override suspend fun changeBalanceAmount(currencyCode: String, amount: Float) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBalance(currencyCode: String) {
        TODO("Not yet implemented")
    }
}