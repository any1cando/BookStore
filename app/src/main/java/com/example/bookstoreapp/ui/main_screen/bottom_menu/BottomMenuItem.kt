package com.example.bookstoreapp.ui.main_screen.bottom_menu

import com.example.bookstoreapp.R

sealed class BottomMenuItem(
    val route: String,
    val title: String,
    val iconId: Int,
) {
    data object Home : BottomMenuItem(
        route = "",
        title = "Home",
        iconId = R.drawable.ic_home
    )

    data object Favorites : BottomMenuItem(
        route = "",
        title = "Favorites",
        iconId = R.drawable.ic_favorite
    )

    data object Settings : BottomMenuItem(
        route = "",
        title = "Settings",
        iconId = R.drawable.ic_settings
    )
}