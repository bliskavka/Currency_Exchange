package com.example.currencyexchange.di

import com.example.currencyexchange.data.remote.ExchangeRateApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    fun provideExchangeRateApi(retrofit: Retrofit) : ExchangeRateApi =
        retrofit.create(ExchangeRateApi::class.java)

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
            .baseUrl("https://developers.paysera.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    fun provideOkHttpClient() =
        OkHttpClient().newBuilder().build()
}