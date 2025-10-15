package com.example.stashstuff.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stashstuff.data.AppDatabase
import com.example.stashstuff.models.Container
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContainerViewModel : ViewModel() {
    private val _containerList = MutableStateFlow<MutableList<Container>>(mutableListOf())
    private val _selectedContainer = MutableStateFlow(Container())

    var containerList: StateFlow<List<Container>> = _containerList.asStateFlow()
    var selectedContainer: StateFlow<Container> = _selectedContainer.asStateFlow()

    fun getAllItems(context: Context) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)
            val items = db.ContainerDao().getAll()

            _containerList.update { items }
        }
    }

    fun getAllContainersFromPlaceId(context: Context, placeId: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            val db = AppDatabase.getDatabase(context)
            val items = db.ContainerDao().getAllContainersFromPlace(placeId)

            _containerList.update { items }
        }
    }

    fun insertItems(context: Context, container: Container) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)

            val newItemId: Long = db.ContainerDao().insertItem(container)
            container.id = newItemId.toInt()

            _containerList.update { (it + listOf(container)).toMutableList() }
        }
    }

    fun deleteItem(context: Context, container: Container) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)

            //Get all subcontainers from container
            val subContainerList = db.SubContainerDao().getAllSubContainersFromContainer(container.id)
            //delete all storageItems from subcontainers
            for (subContainer in subContainerList) {
                db.SubContainerDao().deleteStorageItemsFromSubContainer(subContainer.id)
            }
            //delete all subContainers from Container
            db.ContainerDao().deleteSubContainerFromContainer(container.id)

            db.ContainerDao().deleteItem(container)

            _containerList.update {
                (it - listOf(container).toSet()).toMutableList()
            }
        }
    }

    fun deleteSelectedItem(context: Context) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val containerToDelete =
                _selectedContainer.value

            deleteItem(context, containerToDelete)

        }
    }

    fun getItemById(context: Context, id: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)

            val item = db.ContainerDao().getItemById(id)

            _selectedContainer.update { item }
        }
    }

}

