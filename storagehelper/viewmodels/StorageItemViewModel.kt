package com.example.stashstuff.viewmodels

    import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stashstuff.data.AppDatabase
import com.example.stashstuff.models.StorageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StorageItemViewModel : ViewModel() {
    private val _storageItemList = MutableStateFlow<MutableList<StorageItem>>(mutableListOf())
    private val _selectedStorageItem = MutableStateFlow(StorageItem())
//    private val _currentSubContainer: SubContainer = SubContainer()

    var storageItemList: StateFlow<List<StorageItem>> = _storageItemList.asStateFlow()
    var selectedStorageItem: StateFlow<StorageItem> = _selectedStorageItem.asStateFlow()

    fun getAllItems(context: Context) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)
            val items = db.StorageItemDao().getAll()
            _storageItemList.update { items }
        }
    }

    fun insertItems(context: Context, title: String, amount: Int, subContainerId: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val newItem = StorageItem(itemName = title, itemAmount = amount, subContainerId = subContainerId)
            val db = AppDatabase.getDatabase(context)

            val newItemId: Long = db.StorageItemDao().insertItem(newItem)
            newItem.id = newItemId.toInt()

            _storageItemList.update { (it + listOf(newItem)).toMutableList() }
        }
    }

    fun deleteItem(context: Context, storageItem: StorageItem) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            val db = AppDatabase.getDatabase(context)

            db.StorageItemDao().deleteItem(storageItem)

            _storageItemList.update {
                (it - listOf(storageItem).toSet()).toMutableList()
            }
        }
    }

    fun getItemById(context: Context, id: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            val db = AppDatabase.getDatabase(context)

            val item = db.StorageItemDao().getItemById(id)

            _selectedStorageItem.update { item }
        }
    }

    fun getAllItemsFromSubContainerId(context: Context, subContainerId: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)
            val items = db.StorageItemDao().getAllItemsFromSubContainerId(subContainerId)
            _storageItemList.update { items }
        }
    }
}