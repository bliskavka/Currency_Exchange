package com.example.currencyexchange.data.repository.impl

import com.example.currencyexchange.core.Resource
import com.example.currencyexchange.data.local.UserBalanceDao
import com.example.currencyexchange.data.mapper.UserBalanceLocalDataSource
import com.example.currencyexchange.data.mapper.UserBalanceModelMapper
import com.example.currencyexchange.data.repository.UserBalanceRepository
import com.example.currencyexchange.domain.model.UserBalanceModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserBalanceRepositoryImpl @Inject constructor(
    private val userBalanceLocalDataSource: UserBalanceLocalDataSource
): UserBalanceRepository {
    override suspend fun getAllBalances(): Resource<List<UserBalanceModel>> {
        return withContext(Dispatchers.IO) {
            return@withContext userBalanceLocalDataSource.getAllEntries()
        }
    }

    override suspend fun getBaseBalance(): Resource<UserBalanceModel> {
        val baseCurrencyCode = "EUR" // TODO get from shared preferences or other source
        return  withContext(Dispatchers.IO) {
            return@withContext userBalanceLocalDataSource.getEntryByCode(baseCurrencyCode)
        }
    }

    override suspend fun getBalanceByCode(currencyCode: String): Resource<UserBalanceModel> {
        return  withContext(Dispatchers.IO) {
            return@withContext userBalanceLocalDataSource.getEntryByCode(currencyCode)
        }
    }

    override suspend fun createNewBalance(userBalanceModel: UserBalanceModel) {
        withContext(Dispatchers.IO) {
            userBalanceLocalDataSource.insertEntry(userBalanceModel)
        }
    }

    override suspend fun changeBalanceAmount(userBalanceModel: UserBalanceModel) {
        withContext(Dispatchers.IO) {
            userBalanceLocalDataSource.updateEntry(userBalanceModel)
        }
    }

    override suspend fun deleteBalance(userBalanceModel: UserBalanceModel) {
        withContext(Dispatchers.IO) {
            userBalanceLocalDataSource.deleteEntry(userBalanceModel)
        }
    }
}