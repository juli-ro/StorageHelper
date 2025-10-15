package com.example.stashstuff.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stashstuff.models.Place


@Dao
interface PlaceDao {
    @Query("Select * FROM place")
    fun getAll(): MutableList<Place>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(place: Place) : Long

    @Query("SELECT * FROM place WHERE id = :id")
    fun getItemById(id: Int): Place

    @Delete
    fun deleteItem(place: Place)

    @Query("Delete From container WHERE placeid = :id")
    suspend fun deleteContainersFromPlace(id: Int)
}