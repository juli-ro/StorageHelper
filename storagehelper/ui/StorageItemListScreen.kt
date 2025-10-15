package com.example.stashstuff.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.stashstuff.models.StorageItem
import com.example.stashstuff.models.SubContainer
import com.example.stashstuff.ui.components.InsertItemWithAmount
import com.example.stashstuff.viewmodels.StorageItemViewModel

@Composable
fun StorageItemListScreen(
    subContainer: SubContainer,
    storageItemViewModel: StorageItemViewModel,
    navToDetail: () -> Unit,
    navToParentContainer: () -> Unit
) {
    val context = LocalContext.current

    var itemNameInput by remember { mutableStateOf("") }
    var itemAmountInput by remember { mutableStateOf("") }

    LaunchedEffect(key1 = storageItemViewModel) {
        //Todo: check the use of this
        // if (storageItemViewModel.storageItemList.value.any()) return@LaunchedEffect
        storageItemViewModel.getAllItemsFromSubContainerId(context, subContainer.id)
    }
    val itemList = storageItemViewModel.storageItemList.collectAsState()


    Column(modifier = Modifier.height(500.dp)) {

        Button(onClick = navToParentContainer) {
            Text(text = "back")
        }
        
        Text(text = "SubContainer: ${subContainer.subContainerName}")

        InsertItemWithAmount(
            itemNameInput,
            itemAmountInput,
            { itemNameInput = it },
            {
                if (it.isEmpty()) {
                    itemAmountInput = ""
                } else if (it.toIntOrNull() != null) {
                    itemAmountInput = it
                }
            })

        LazyColumn(modifier = Modifier, userScrollEnabled = true) {
            items(itemList.value) { item ->
                StashListItem(
                    storageItem = item,
                    deleteItem = { storageItemViewModel.deleteItem(context, item) },
                    navToDetail = {
                        storageItemViewModel.getItemById(context, item.id)
                        navToDetail()
                    }
                )
            }
        }
        Button(
            onClick = {
                if (itemAmountInput.isEmpty()) {
                    storageItemViewModel.insertItems(
                        context,
                        itemNameInput,
                        0,
                        subContainerId = subContainer.id
                    )
                } else {
                    storageItemViewModel.insertItems(
                        context,
                        itemNameInput,
                        itemAmountInput.toInt(),
                        subContainerId = subContainer.id
                    )
                }
            },
            modifier = Modifier
                .height(50.dp)
        ) {
            Text(text = "Tap")
        }
    }

}

@Composable
fun StashListItem(
    storageItem: StorageItem,
    deleteItem: () -> Unit,
    navToDetail: () -> Unit
) {
    Row(Modifier.fillMaxWidth()) {
        Text(
            text = storageItem.id.toString(),
            modifier = Modifier.weight(2f)
        )
        Text(
            text = storageItem.itemName,
            modifier = Modifier.weight(2f)
        )
        Text(
            text = storageItem.itemAmount.toString(),
            modifier = Modifier.weight(2f)
        )
        Button(
            onClick = deleteItem, modifier = Modifier
                .width(60.dp)
                .weight(1.2f)
        ) {
            Text(text = "X")
        }
        Button(
            onClick = navToDetail, modifier = Modifier
                .width(60.dp)
                .weight(1.8f)
        ) {
            Text(text = "Edit")
        }
    }
}


