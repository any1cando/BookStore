package com.example.bookstoreapp.dto

data class Book(
    val isFavorite: Boolean = false,
    val key: String = "ExampleOfKey",
    val name: String = "BookName",
    val description: String = "BookDescription",
    val price: String = "BookPrice",
    val category: String = "BookCategory",
    val imageUrl: String = "BookImageUrl"
)
