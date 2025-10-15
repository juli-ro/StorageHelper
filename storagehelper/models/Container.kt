package com.example.stashstuff.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "container")
data class Container (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val containerName: String = "",
    val placeId: Int = 0
)
