package com.example.currencyexchange

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currencyexchange.data.local.UserBalanceDao
import com.example.currencyexchange.data.local.UserBalanceEntity

@Database(
    entities = [UserBalanceEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserBalanceDao(): UserBalanceDao

}