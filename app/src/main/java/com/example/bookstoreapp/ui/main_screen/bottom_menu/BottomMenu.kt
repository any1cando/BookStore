package com.example.bookstoreapp.ui.main_screen.bottom_menu

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource


@Composable
fun BottomMenu(
    selectedItem: String,
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit
) {
    val items = listOf(
        BottomMenuItem.Home,
        BottomMenuItem.Favorites,
        BottomMenuItem.Settings
    )

    NavigationBar() {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item.title,
                onClick = {
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