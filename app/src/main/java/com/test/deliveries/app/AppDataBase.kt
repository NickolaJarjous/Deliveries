package com.test.deliveries.app

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.deliveries.database.dao.DeliveriesDao
import com.test.deliveries.database.tables.Delivery

@Database(entities = [Delivery::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deliveriesDao(): DeliveriesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    AppConsts.DATABASE_NAME
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }

    }


}