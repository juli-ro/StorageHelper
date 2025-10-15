package com.example.stashstuff.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "place")
data class Place(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val placeName: String = "",
)
