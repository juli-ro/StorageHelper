package com.example.stashstuff.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "subContainer")
data class SubContainer(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val subContainerName: String = "",
    val containerId: Int = 0,)

data class SubContainerWithStorageItems(
    @Embedded val subContainer: SubContainer,
    @Relation(
        parentColumn = "id",
        entityColumn = "subContainerId"
    )
    val storageItems: List<StorageItem>
)