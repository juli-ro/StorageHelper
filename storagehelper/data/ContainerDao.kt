package com.example.stashstuff.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stashstuff.models.Container

@Dao
interface ContainerDao {
    @Query("Select * FROM container")
    fun getAll(): MutableList<Container>

    @Query("Select * FROM container Where placeId = :placeId")
    fun getAllContainersFromPlace(placeId: Int) : MutableList<Container>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(container: Container) : Long

    @Query("SELECT * FROM container WHERE id = :id")
    fun getItemById(id: Int): Container

    @Delete
    fun deleteItem(container: Container)

    @Query("Delete From subContainer WHERE containerId = :id")
    suspend fun deleteSubContainerFromContainer(id: Int)
}