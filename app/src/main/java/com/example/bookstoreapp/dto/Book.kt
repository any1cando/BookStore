package com.example.bookstoreapp.dto

data class Book(
    val key: String = "ExampleOfKey",
    val name: String = "BookName",
    val description: String = "BookDescription",
    val price: String = "BookPrice",
    val category: String = "BookCategory",
    val imageUrl: String = "BookImageUrl"
)
