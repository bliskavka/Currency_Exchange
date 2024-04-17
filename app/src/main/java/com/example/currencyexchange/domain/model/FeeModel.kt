package com.example.currencyexchange.domain.model

data class FeeModel (
    val percentage: Float? = null,
    val fixedValue: Float? = null
) {
    fun getValue(amount: Float) : Float {
        percentage?.let {
            return amount * percentage / 100
        }
        fixedValue?.let {
            return fixedValue
        }
        return 0F
    }

    companion object {
        fun none() = FeeModel(fixedValue = 0F)
    }
}