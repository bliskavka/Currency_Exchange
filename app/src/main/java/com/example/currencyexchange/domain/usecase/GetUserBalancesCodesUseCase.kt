package com.example.currencyexchange.domain.usecase

import com.example.currencyexchange.data.repository.UserBalanceRepository
import javax.inject.Inject

class GetUserBalancesCodesUseCase @Inject constructor(
    private val userBalanceRepository: UserBalanceRepository
) {
    suspend operator fun invoke(): List<String> {
        val allEntries = userBalanceRepository.getAllBalances()

        return if (allEntries.isSuccess()) {
            allEntries.successValue().map {
                it.currencyCode
            }
        } else {
            listOf()
        }
    }
}