package com.example.stashstuff.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.stashstuff.models.StorageItem
import kotlinx.coroutines.flow.Flow

@Dao
interface StorageItemDao {
    @Query("SELECT * FROM storageItem")
    fun getAll(): MutableList<StorageItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(storageItems: StorageItem) : Long

    @Delete
    fun deleteItem(storageItems: StorageItem)

    @Query("SELECT * FROM storageItem WHERE id = :id")
    fun getItemById(id: Int): StorageItem

    @Transaction
    @Query("Select * FROM storageItem WHERE subContainerId = :id")
    fun getAllItemsFromSubContainerId(id: Int): MutableList<StorageItem>

}