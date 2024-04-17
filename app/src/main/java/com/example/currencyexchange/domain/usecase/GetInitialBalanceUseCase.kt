package com.example.currencyexchange.domain.usecase

import android.database.sqlite.SQLiteConstraintException
import com.example.currencyexchange.data.repository.UserBalanceRepository
import com.example.currencyexchange.domain.model.UserBalanceModel
import javax.inject.Inject

class GetInitialBalanceUseCase @Inject constructor(
    private val userBalanceRepository: UserBalanceRepository,
) {
    suspend operator fun invoke(): Boolean {
        try {
            userBalanceRepository.createNewBalance(
                UserBalanceModel(
                    amount = 10000F,
                    currencyCode = "EUR"
                )
            )
        } catch (_: SQLiteConstraintException) {
            return false
        }
        return true
    }
}