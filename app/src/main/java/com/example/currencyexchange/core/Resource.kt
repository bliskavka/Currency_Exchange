package com.example.currencyexchange.core

import java.lang.Exception

class Resource<T> private constructor(
    private val successValue: T? = null,
    private val failureValue: Failure? = null
) {

    fun isSuccess(): Boolean {
        return failureValue == null
    }

    fun isFailure(): Boolean {
        return !isSuccess()
    }

    fun successValue(): T? = successValue

    fun failureValue(): Failure = failureValue!!

    companion object {

        fun success(): Resource<Nothing> = Resource()

        fun <T> success(value: T): Resource<T> = Resource(successValue = value)

        fun <T> failure(failure: Failure): Resource<T> = Resource(failureValue = failure)

        fun <T> failure(exception: Exception): Resource<T> = Resource(failureValue = Failure(exception = exception))
    }
}

data class Failure(val message: String? = null,
                   val exception: Exception = Exception())
