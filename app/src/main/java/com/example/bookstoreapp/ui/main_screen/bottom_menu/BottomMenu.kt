package com.example.bookstoreapp.ui.main_screen.bottom_menu

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource


@Composable
fun BottomMenu(
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit
) {
    val items = listOf(
        BottomMenuItem.Home,
        BottomMenuItem.Favorites,
        BottomMenuItem.Settings
    )

    val selectedItem = remember {
        mutableStateOf("Home")  // Запоминаем, что первым выбран экран Home в BottomBar
    }

    NavigationBar() {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedItem.value == item.title,
                onClick = {
                    selectedItem.value = item.title
                    when(item.title) {
                        BottomMenuItem.Home.title -> onHomeClick()
                        BottomMenuItem.Favorites.title -> onFavoritesClick()
                    }

                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconId),
                        contentDescription = "Item Icon"
                    )
                },
                label = { Text(text = item.title) }
            )
        }
    }
}