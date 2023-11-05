package com.demo.app.countries_list.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.demo.app.countries_list.data.local.entity.CountryEntity

@Dao
interface CountriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(country: CountryEntity)

    @Query("SELECT * FROM countries")
    fun getAllCountries(): List<CountryEntity>
}