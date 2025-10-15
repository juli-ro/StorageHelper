package com.example.stashstuff.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.stashstuff.models.SubContainer
import com.example.stashstuff.models.SubContainerWithStorageItems

@Dao
interface SubContainerDao {
    @Query("SELECT * FROM subContainer")
    fun getAll(): MutableList<SubContainer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(subContainer: SubContainer) : Long

    @Delete
    fun deleteItem(subContainer: SubContainer)

    //Todo: probably rework this so it works with room database
    @Query("Delete From storageItem WHERE subContainerId = :id")
    suspend fun deleteStorageItemsFromSubContainer(id: Int)

    //Todo: probably rework this so it works with room database
    @Query("SELECT * FROM subContainer WHERE containerId = :containerId")
    suspend fun getAllSubContainersFromContainer(containerId: Int): MutableList<SubContainer>

    @Query("SELECT * FROM subContainer WHERE id = :id")
    fun getItemById(id: Int): SubContainer

    @Transaction
    @Query("SELECT * FROM subContainer")
    fun getSubContainersWithStorageItems(): MutableList<SubContainerWithStorageItems>
}