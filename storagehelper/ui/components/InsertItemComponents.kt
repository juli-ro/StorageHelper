package com.example.stashstuff.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun InsertItemWithAmount(
    itemNameInput: String,
    itemAmountInput: String,
    textChanged: (String) -> Unit,
    amountChanged: (String) -> Unit
) {
    Row {
        TextField(
            value = itemNameInput,
            placeholder = { Text("item") },
            onValueChange = { textChanged(it) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true
        )
        TextField(
            value = itemAmountInput,
            placeholder = { Text("0") },
            onValueChange = { amountChanged(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            singleLine = true
        )
    }
}

@Composable
fun InsertSimpleItem(
    itemNameInput: String,
    textChanged: (String) -> Unit,
    addItem: () -> Unit
) {
    Row {
        TextField(
            value = itemNameInput,
            placeholder = { Text("new item name") },
            onValueChange = { textChanged(it) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true
        )
        Button(onClick = addItem) {
            Text(text = "+")

        }
    }
}