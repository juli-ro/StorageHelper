package com.example.stashstuff.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stashstuff.data.AppDatabase
import com.example.stashstuff.models.SubContainer
import com.example.stashstuff.models.SubContainerWithStorageItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubContainerViewModel : ViewModel() {
    private val _subContainerList = MutableStateFlow<MutableList<SubContainer>>(mutableListOf())
    private val _selectedSubContainer = MutableStateFlow(SubContainer())
    private val _subContainerWithStorageItemsList = MutableStateFlow<MutableList<SubContainerWithStorageItems>>(mutableListOf())

    
    var subContainerList: StateFlow<List<SubContainer>> = _subContainerList.asStateFlow()
    var selectedSubContainer: StateFlow<SubContainer> = _selectedSubContainer.asStateFlow()
    var subContainerWithStorageItemsList: StateFlow<MutableList<SubContainerWithStorageItems>> = _subContainerWithStorageItemsList.asStateFlow()

    fun getAllSubContainers(context: Context) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)
            val items: MutableList<SubContainer> = db.SubContainerDao().getAll()

            _subContainerList.update { items }
        }
    }

    fun getAllSubContainersFromContainer(context: Context, containerId: Int) = viewModelScope.launch{
        withContext(Dispatchers.IO){
            val db = AppDatabase.getDatabase(context)
            val items: MutableList<SubContainer> = db.SubContainerDao().getAllSubContainersFromContainer(containerId)

            _subContainerList.update { items }
        }

    }

    fun insertItems(context: Context, subContainer: SubContainer) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)

            val newItemId: Long = db.SubContainerDao().insertItem(subContainer)
            subContainer.id = newItemId.toInt()

            _subContainerList.update { (it + listOf(subContainer)).toMutableList() }
        }
    }

    fun deleteItem(context: Context, subContainer: SubContainer) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            val db = AppDatabase.getDatabase(context)

            db.SubContainerDao().deleteStorageItemsFromSubContainer(subContainer.id)

            db.SubContainerDao().deleteItem(subContainer)

            _subContainerList.update {
                (it - listOf(subContainer).toSet()).toMutableList()
            }
        }
    }

    fun deleteSelectedItem(context: Context) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            val db = AppDatabase.getDatabase(context)

            val subContainerToDelete = selectedSubContainer.value

            db.SubContainerDao().deleteStorageItemsFromSubContainer(subContainerToDelete.id)

            db.SubContainerDao().deleteItem(subContainerToDelete)

            _subContainerList.update {
                (it - listOf(subContainerToDelete).toSet()).toMutableList()
            }
        }
    }

    fun getItemById(context: Context, id: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            val db = AppDatabase.getDatabase(context)

            val item = db.SubContainerDao().getItemById(id)

            _selectedSubContainer.update { item }
        }
    }

    fun getAllStorageItemsInSubContainer(context: Context) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            val db = AppDatabase.getDatabase(context)
            val storageItemsInSubContainer: MutableList<SubContainerWithStorageItems> = db.SubContainerDao().getSubContainersWithStorageItems()

            _subContainerWithStorageItemsList.update { storageItemsInSubContainer }
        }
    }
}