package com.example.stashstuff.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "storageItem")
data class StorageItem(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val itemName: String = "",
    val itemAmount: Int = 0,
    //Todo: Check if it is ok to be "Nullable"
    val subContainerId: Int = 0,
)

