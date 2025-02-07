package com.example.bookstoreapp.ui.login_screen.data

import kotlinx.serialization.Serializable

@Serializable
data class MainScreenDataObject(
    val uid: String,
    val email: String
)
