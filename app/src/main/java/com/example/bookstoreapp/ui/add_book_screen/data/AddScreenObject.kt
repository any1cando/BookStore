package com.example.bookstoreapp.ui.add_book_screen.data

import kotlinx.serialization.Serializable

@Serializable
data class AddScreenObject(
    val key: String = "ExampleOfKey",
    val name: String = "BookName",
    val description: String = "BookDescription",
    val price: String = "BookPrice",
    val category: String = "BookCategory",
    val imageUrl: String = "BookImageUrl"
)