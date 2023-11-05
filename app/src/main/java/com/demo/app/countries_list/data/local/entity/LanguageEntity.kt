package com.demo.app.countries_list.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "language")
data class LanguageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val code: String?,
    val name: String?
)