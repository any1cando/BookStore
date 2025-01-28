package com.example.bookstoreapp.dto

import java.lang.invoke.TypeDescriptor

data class Book(
    val name: String = "BookName",
    val description: String = "BookDescription",
    val price: String = "BookPrice",
    val category: String = "BookCategory",
    val imageUrl: String = "BookImageUrl"
)
