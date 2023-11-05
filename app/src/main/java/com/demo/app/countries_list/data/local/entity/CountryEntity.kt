package com.demo.app.countries_list.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val capital: String?,
    val code: String?,
    val currencyId: Int,
    val flag: String?,
    val languageId: Int?,
    val name: String?,
    val region: String?
)