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
import com.example.stashstuff.models.Container
import com.example.stashstuff.models.Place
import com.example.stashstuff.ui.components.InsertSimpleItem
import com.example.stashstuff.ui.components.SimpleAlertDialog
import com.example.stashstuff.viewmodels.ContainerViewModel

@Composable
fun ContainerScreen(
    selectedPlace: Place,
    navToSubContainerItemList: (Container) -> Unit,
    navToParent: () -> Unit
) {
    ContainerMainScreen(
        place = selectedPlace,
        navToSelectedItem = navToSubContainerItemList,
        navToParent = navToParent
    )
}


@Composable
fun ContainerMainScreen(
    containerViewModel: ContainerViewModel = viewModel(),
    place: Place,
    navToSelectedItem: (Container) -> Unit,
    navToParent: () -> Unit
) {
    val context = LocalContext.current
    val containerList = containerViewModel.containerList.collectAsState()
    containerViewModel.getAllContainersFromPlaceId(context, place.id)

    var containerNameInput by remember { mutableStateOf("") }
    var openDeleteDialog by remember {
        mutableStateOf(false)
    }

    if (openDeleteDialog) {
        SimpleAlertDialog(
            onDismissRequest = { openDeleteDialog = false },
            onConfirmation = {
                containerViewModel.deleteSelectedItem(context)
                openDeleteDialog = false
            },
            dialogTitle = "Delete Container?",
            dialogText = "Deleting the Container will delete all contained Items",
        )
    }

    Column(modifier = Modifier) {

        Button(onClick = navToParent) {
            Text(text = "back")
        }

        Text(text = "Room: ${place.placeName}")

        InsertSimpleItem(
            itemNameInput = containerNameInput,
            textChanged = { containerNameInput = it },
            addItem = {
                val containerToInsert = Container(
                    containerName = containerNameInput,
                    placeId = place.id
                )
                containerViewModel.insertItems(context, containerToInsert)
            }
        )

        LazyColumn(modifier = Modifier, userScrollEnabled = true) {
            items(containerList.value) { container ->
                ContainerItem(
                    container = container,
                    onItemClicked = {
                        navToSelectedItem(container)
                    },
                    deleteContainer = {
                        containerViewModel.getItemById(context, container.id)
                        openDeleteDialog = true
                    }

                )

            }
        }
    }
}

@Composable
fun ContainerItem(
    container: Container,
    onItemClicked: () -> Unit,
    deleteContainer: () -> Unit
) {
    Row(modifier = Modifier
        .clickable { onItemClicked.invoke() }
        .padding(16.dp)
        .fillMaxWidth()) {
        Text(
            text = container.containerName,
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