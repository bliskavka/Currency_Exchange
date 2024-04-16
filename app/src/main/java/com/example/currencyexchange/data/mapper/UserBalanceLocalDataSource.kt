package com.example.currencyexchange.data.mapper

import com.example.currencyexchange.core.Resource
import com.example.currencyexchange.data.local.UserBalanceDao
import com.example.currencyexchange.data.mapper.ExchangeRateModelMapper
import com.example.currencyexchange.domain.model.CurrencyRateModel
import com.example.currencyexchange.domain.model.UserBalanceModel
import javax.inject.Inject

class UserBalanceLocalDataSource @Inject constructor(
    private val userBalanceDao: UserBalanceDao,
    private val modelMapper: UserBalanceModelMapper,
) {

    suspend fun insertEntry(balanceEntry: UserBalanceModel) {
        val entity = modelMapper.toDao(balanceEntry)
        userBalanceDao.insertBalance(entity)
    }

    suspend fun updateEntry(balanceEntry: UserBalanceModel) {
        val entity = modelMapper.toDao(balanceEntry)
        userBalanceDao.updateBalance(entity)
    }

    suspend fun deleteEntry(balanceEntry: UserBalanceModel) {
        val entity = modelMapper.toDao(balanceEntry)
        userBalanceDao.deleteBalance(entity)
    }

    suspend fun getAllEntries(): Resource<List<UserBalanceModel>> {
        return try {
            val entry = userBalanceDao.getAllEntities()
            val model = modelMapper.fromDao(entry)
            Resource.success(model)
        } catch (e: Exception) {
            Resource.failure(e)
        }
    }

    suspend fun getEntryByCode(currencyCode: String): Resource<UserBalanceModel> {
        return try {
            val entry = userBalanceDao.getEntityByCode(currencyCode)
            val model = modelMapper.fromDao(entry)
            Resource.success(model)
        } catch (e: Exception) {
            Resource.failure(e)
        }
    }
}