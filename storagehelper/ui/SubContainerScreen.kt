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
import com.example.stashstuff.models.SubContainer
import com.example.stashstuff.ui.components.InsertSimpleItem
import com.example.stashstuff.ui.components.SimpleAlertDialog
import com.example.stashstuff.viewmodels.SubContainerViewModel

@Composable
fun SubContainerScreen(
    container: Container,
    navToStorageItemList: (SubContainer) -> Unit,
    navToParentContainer: () -> Unit
) {
    SubContainerMainScreen(
        navToSelectedItem = navToStorageItemList,
        container = container,
        navToParentContainer = navToParentContainer
    )
}

@Composable
fun SubContainerMainScreen(
    subContainerViewModel: SubContainerViewModel = viewModel(),
    navToSelectedItem: (SubContainer) -> Unit,
    container: Container,
    navToParentContainer: () -> Unit
) {

    val context = LocalContext.current
    val subContainerList = subContainerViewModel.subContainerList.collectAsState()
    subContainerViewModel.getAllSubContainersFromContainer(
        context,
        container.id
    )

    var subContainerNameInput by remember { mutableStateOf("") }
    var openDeleteDialog by remember {
        mutableStateOf(false)
    }


    if (openDeleteDialog) {
        SimpleAlertDialog(
            onDismissRequest = { openDeleteDialog = false },
            onConfirmation = {
                subContainerViewModel.deleteSelectedItem(context)
                openDeleteDialog = false
            },
            dialogTitle = "Delete Subcontainer?",
            dialogText = "Deleting the Subcontainer will delete all contained Items",
        )
    }

    Column(
        modifier = Modifier
    ) {
        Button(onClick = navToParentContainer){
            Text(text = "back")
        }
        
        Text(
            text = "Container: ${container.containerName}"
        )
        InsertSimpleItem(
            itemNameInput = subContainerNameInput,
            textChanged = { subContainerNameInput = it },
            addItem = {
                val newSubContainer = SubContainer(
                    subContainerName = subContainerNameInput,
                    containerId = container.id
                )
                subContainerViewModel.insertItems(context, newSubContainer)
            }
        )
        LazyColumn(modifier = Modifier, userScrollEnabled = true) {
            items(subContainerList.value) { subContainer ->
                SubContainerItem(
                    subContainer = subContainer,
                    onItemClicked = {
                        navToSelectedItem(subContainer)
                    },
                    deleteSubContainer = {
                        subContainerViewModel.getItemById(context, subContainer.id)
                        openDeleteDialog = true
                    }
                )
            }
        }
    }
}

@Composable
fun SubContainerItem(
    subContainer: SubContainer,
    onItemClicked: () -> Unit,
    deleteSubContainer: () -> Unit
) {
    Row(modifier = Modifier
        .clickable { onItemClicked.invoke() }
        .padding(16.dp)
        .fillMaxWidth()) {
        Text(
            text = subContainer.subContainerName,
            modifier = Modifier.weight(0.8f)
        )
        Button(
            onClick = deleteSubContainer,
            modifier = Modifier.weight(0.2f)
        ) {
            Text(text = "X")
        }
    }
}

