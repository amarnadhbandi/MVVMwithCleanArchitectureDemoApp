package com.demo.app.shared.data.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.demo.app.countries_list.data.local.CountriesDao
import com.demo.app.countries_list.data.local.entity.CountryEntity

@Database(entities = [CountryEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase()  {
    abstract fun countryDao(): CountriesDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            tempInstance?.let { return it }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "countries_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}