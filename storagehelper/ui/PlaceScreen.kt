package com.example.stashstuff.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stashstuff.models.Place
import com.example.stashstuff.ui.components.InsertSimpleItem
import com.example.stashstuff.ui.components.SimpleAlertDialog
import com.example.stashstuff.viewmodels.PlaceViewModel

@Composable
fun PlaceScreen(navToContainerItemList: (Place) -> Unit){
    PlaceMainScreen(navToSelectedItem = navToContainerItemList)
}

@Composable
fun PlaceMainScreen(
    placeViewModel: PlaceViewModel = viewModel(),
    navToSelectedItem: (Place) -> Unit
){
    val context = LocalContext.current
    val placeList = placeViewModel.placeList.collectAsState()
    placeViewModel.getAllItems(context)

    var placeNameInput by remember { mutableStateOf("") }
    var openDeleteDialog by remember {
        mutableStateOf(false)
    }

    if (openDeleteDialog) {
        SimpleAlertDialog(
            onDismissRequest = { openDeleteDialog = false },
            onConfirmation = {
                placeViewModel.deleteSelectedItem(context)
                openDeleteDialog = false
            },
            dialogTitle = "Delete Room?",
            dialogText = "Deleting the Room will delete all contained Items",
        )
    }

    Column(modifier = Modifier){
        Text(text = "Place Main Screen")

        InsertSimpleItem(
            itemNameInput = placeNameInput,
            textChanged = { placeNameInput = it },
            addItem = {
                val placeToInsert = Place(placeName = placeNameInput)
                placeViewModel.insertItems(context, placeToInsert)
            }
        )

        LazyColumn(modifier = Modifier, userScrollEnabled = true) {
            items(placeList.value) { place ->
                PlaceItem(
                    place = place,
                    onItemClicked = {
                        navToSelectedItem(place)
                    },
                    deleteContainer = {
                        placeViewModel.getItemById(context, place.id)
                        openDeleteDialog = true
                    }

                )

            }
        }

    }
}

@Composable
fun PlaceItem(
    place: Place,
    onItemClicked: () -> Unit,
    deleteContainer: () -> Unit
) {
    Row(modifier = Modifier
        .clickable { onItemClicked.invoke() }
        .padding(16.dp)
        .fillMaxWidth()) {
        Text(
            text = place.placeName,
            modifier = Modifier.weight(0.8f)
        )
        Button(
            onClick = deleteContainer,
            modifier = Modifier.weight(0.2f)
        ) {
            Text(text = "X")
        }
    }
}