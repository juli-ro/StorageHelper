package com.example.stashstuff

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stashstuff.models.Container
import com.example.stashstuff.models.Place
import com.example.stashstuff.models.SubContainer
import com.example.stashstuff.ui.StashItemDetailScreen
import com.example.stashstuff.ui.StorageItemListScreen
import com.example.stashstuff.ui.SubContainerScreen
import com.example.stashstuff.ui.theme.StashstuffTheme
import com.example.stashstuff.ui.ContainerScreen
import com.example.stashstuff.ui.PlaceScreen
import com.example.stashstuff.viewmodels.StorageItemViewModel


enum class NavScreens {
    StorageItemList,
    StorageItemDetail,
    SubContainerList,
    ContainerList,
    PlaceList,
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StashstuffTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    StashStuffApp()
                }
            }
        }
    }
}

@Preview
@Composable
fun StashStuffPreview() {
    StashStuffApp()
}


@Composable
fun StashStuffApp(
    storageItemViewModel: StorageItemViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    
    val selectedItem = storageItemViewModel.selectedStorageItem.collectAsState()

    var selectedSubContainer: SubContainer by remember {
        mutableStateOf(SubContainer())
    }

    var selectedContainer: Container by remember {
        mutableStateOf(Container())
    }

    var selectedPlace: Place by remember {
        mutableStateOf(Place())
    }

    NavHost(
        navController = navController,
        startDestination = NavScreens.PlaceList.name,
        modifier = Modifier
    ) {
        composable(route = NavScreens.PlaceList.name){
            PlaceScreen(navToContainerItemList = {
                selectedPlace = it
                navController.navigate(NavScreens.ContainerList.name)
            })
        }
        composable(route = NavScreens.ContainerList.name){
            ContainerScreen(
                selectedPlace = selectedPlace,
                navToSubContainerItemList = {
                    selectedContainer = it
                    navController.navigate(NavScreens.SubContainerList.name)
                },
                navToParent = { navController.navigate(NavScreens.PlaceList.name)}

            )
        }
        composable(route = NavScreens.SubContainerList.name){
            SubContainerScreen(
                container = selectedContainer,
                navToStorageItemList = {
                    selectedSubContainer = it
                    navController.navigate(NavScreens.StorageItemList.name)
                },
                navToParentContainer = { navController.navigate(NavScreens.ContainerList.name)}
            )
        }
        composable(route = NavScreens.StorageItemList.name) {
            StorageItemListScreen(
                subContainer = selectedSubContainer,
                storageItemViewModel = storageItemViewModel,
                navToDetail = { navController.navigate(NavScreens.StorageItemDetail.name) },
                navToParentContainer = { navController.navigate(NavScreens.SubContainerList.name)}
            )
        }
        composable(route = NavScreens.StorageItemDetail.name) {
            StashItemDetailScreen(storageItem = selectedItem.value) {
                navController.navigate(NavScreens.StorageItemList.name)
            }
        }
    }
}





