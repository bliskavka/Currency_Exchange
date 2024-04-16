package com.example.currencyexchange.data.repository.impl

import com.example.currencyexchange.core.Resource
import com.example.currencyexchange.data.repository.UserBalanceRepository
import com.example.currencyexchange.domain.model.UserBalanceModel
import javax.inject.Inject

class UserBalanceRepositoryMock @Inject constructor(): UserBalanceRepository {
    override suspend fun getAllBalances(): Resource<List<UserBalanceModel>> {
        return Resource.success(listOf(
            UserBalanceModel("GBP", 2300.12F),
            UserBalanceModel("EUR", 10000F),
            UserBalanceModel("USD", 17.3F),
            UserBalanceModel("UAH", 33760F),
        ))
    }

    override suspend fun getBaseBalance(): Resource<UserBalanceModel> {
        return Resource.success(UserBalanceModel("EUR", 10000F))
    }

    override suspend fun getBalanceByCode(currencyCode: String): Resource<UserBalanceModel> {
        return Resource.success(UserBalanceModel("USD", 17.3F))
    }

    override suspend fun createNewBalance(userBalanceModel: UserBalanceModel) {
        TODO("Not yet implemented")
    }

    override suspend fun changeBalanceAmount(userBalanceModel: UserBalanceModel) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBalance(userBalanceModel: UserBalanceModel) {
        TODO("Not yet implemented")
    }
}