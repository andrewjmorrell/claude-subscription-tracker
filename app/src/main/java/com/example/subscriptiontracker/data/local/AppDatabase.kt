package com.example.subscriptiontracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.subscriptiontracker.data.local.converter.Converters
import com.example.subscriptiontracker.data.local.dao.SubscriptionDao
import com.example.subscriptiontracker.data.local.entity.SubscriptionEntity

@Database(entities = [SubscriptionEntity::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subscriptionDao(): SubscriptionDao
}
