package com.example.stashstuff.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stashstuff.models.StorageItem

@Composable
fun StashItemDetailScreen(storageItem: StorageItem, navigateBack: () -> Unit){
    Column {
        Text(text = storageItem.id.toString())
        Text(text = storageItem.itemName)
        Text(text = storageItem.itemAmount.toString())
        Button(onClick = navigateBack, modifier = Modifier.height(50.dp)) {
            Text(text = "Navigate Back")
        }
    }
}