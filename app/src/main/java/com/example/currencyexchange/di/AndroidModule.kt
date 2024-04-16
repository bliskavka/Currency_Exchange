package com.example.currencyexchange.di

import android.content.Context
import androidx.room.Room
import com.example.currencyexchange.AppDatabase
import com.example.currencyexchange.data.local.UserBalanceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AndroidModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigrationOnDowngrade()
//            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideUserBalanceDao(appDatabase: AppDatabase) : UserBalanceDao {
        return appDatabase.getUserBalanceDao()
    }
}