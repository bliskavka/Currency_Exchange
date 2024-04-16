package com.example.currencyexchange.data.mapper

import com.example.currencyexchange.data.local.UserBalanceEntity
import com.example.currencyexchange.domain.model.UserBalanceModel

class UserBalanceModelMapper {

    fun fromDao(entities: List<UserBalanceEntity>) : List<UserBalanceModel> {
        return entities.map {
            fromDao(it)
        }
    }

    fun fromDao(entity: UserBalanceEntity) : UserBalanceModel {
        return UserBalanceModel(
            currencyCode = entity.currencyCode,
            amount = entity.balanceAmount
        )
    }

    fun toDao(model: UserBalanceModel) : UserBalanceEntity {
        return UserBalanceEntity(
            currencyCode = model.currencyCode,
            balanceAmount = model.amount
        )
    }
}