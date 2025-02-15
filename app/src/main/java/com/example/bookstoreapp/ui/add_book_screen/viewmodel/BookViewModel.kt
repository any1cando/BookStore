package com.example.bookstoreapp.ui.add_book_screen.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.bookstoreapp.dto.Book

class BookViewModel: ViewModel() {
    val currentBook: MutableState<Book?> = mutableStateOf(null)
}