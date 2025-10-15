package com.example.stashstuff.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stashstuff.data.AppDatabase
import com.example.stashstuff.models.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaceViewModel : ViewModel() {
    private val _placeList = MutableStateFlow<MutableList<Place>>(mutableListOf())
    private val _selectedPlace = MutableStateFlow(Place())

    var placeList: StateFlow<List<Place>> = _placeList.asStateFlow()
    var selectedPlace: StateFlow<Place> = _selectedPlace.asStateFlow()

    fun getAllItems(context: Context) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)
            val items = db.PlaceDao().getAll()
            _placeList.update { items }
        }
    }

    fun insertItems(context: Context, place: Place) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)

            val newItemId: Long = db.PlaceDao().insertItem(place)
            place.id = newItemId.toInt()

            _placeList.update { (it + listOf(place)).toMutableList() }
        }
    }

    fun deleteItem(context: Context, place: Place) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)

            //Get all Containers from place
            val containerList = db.ContainerDao().getAllContainersFromPlace(place.id)

            //Get all subContainers from container
            for(container in containerList){

                //Get all storageItems from SubContainer and delete StorageItems (lowest child)
                val subContainerList = db.SubContainerDao().getAllSubContainersFromContainer(container.id)
                for(subContainer in subContainerList){
                    db.SubContainerDao().deleteStorageItemsFromSubContainer(subContainer.id)
                }

                //delete all subContainers after being emptied
                db.ContainerDao().deleteSubContainerFromContainer(container.id)
            }

            //delete all Containers after being emptied
            db.PlaceDao().deleteContainersFromPlace(place.id)

            //delete place after being emptied
            db.PlaceDao().deleteItem(place)

            _placeList.update {
                (it - listOf(place).toSet()).toMutableList()
            }
        }
    }

    fun deleteSelectedItem(context: Context) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val placeToDelete =
                _selectedPlace.value

            deleteItem(context, placeToDelete)

        }
    }

    fun getItemById(context: Context, id: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)

            val item = db.PlaceDao().getItemById(id)

            _selectedPlace.update { item }
        }
    }

}