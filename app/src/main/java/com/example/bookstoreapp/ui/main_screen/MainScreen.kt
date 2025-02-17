package com.example.bookstoreapp.ui.main_screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.bookstoreapp.dto.Book
import com.example.bookstoreapp.dto.Favorite
import com.example.bookstoreapp.ui.login_screen.data.MainScreenDataObject
import com.example.bookstoreapp.ui.main_screen.bottom_menu.BottomMenu
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


@Composable
fun MainScreen(
    navData: MainScreenDataObject,
    onEditBookClick: (Book) -> Unit,
    onAdminClick: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val booksListState = remember {
        mutableStateOf(emptyList<Book>())
    }
    val isAdminState = remember {
        mutableStateOf(false)
    }

    val db = remember {
        Firebase.firestore
    }

    LaunchedEffect(Unit) {
        getAllFavoritesIds(db, navData.uid) { favorites ->
            getAllBooks(
                db,
                favorites,
                onBooks = { books ->
                    booksListState.value = books
                }
            )
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.fillMaxWidth(),
        drawerContent = {
            Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                DrawerHeader(navData.email)
                DrawerBody(
                    onAdmin = { adminState ->
                        isAdminState.value = adminState
                    },
                    onFavoritesClick = {
                        getAllFavoritesIds(db, navData.uid) { favorites ->
                            getAllFavoritesBooks(
                                db,
                                favorites,
                                onBooks = { books ->
                                    booksListState.value = books
                                }
                            )
                        }
                    },
                    onAdminClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        onAdminClick()
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomMenu(
                    onHomeClick = {
                        getAllFavoritesIds(db, navData.uid) { favorites ->
                            getAllBooks(
                                db,
                                favorites,
                                onBooks = { books ->
                                    booksListState.value = books
                                }
                            )
                        }
                    },
                    onFavoritesClick = {
                        getAllFavoritesIds(db, navData.uid) { favorites ->
                            getAllFavoritesBooks(
                                db,
                                favorites,
                                onBooks = { books ->
                                    booksListState.value = books
                                }
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->  // Отступы, чтобы боттом бар не перекрывал наши книги
            // Заполняем основной экран книгами
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(booksListState.value) { book ->
                    BookItem(
                        showEditButton = isAdminState.value,
                        book = book,
                        onEditClick = { clickedBook ->
                            onEditBookClick(clickedBook)
                        },
                        onFavoriteClick = {
                            // С помощью map перебирем изначальный список и делаем изменения на сердечко с проверкой
                            booksListState.value = booksListState.value.map { clickedBook ->
                                if (clickedBook.key == book.key) {
                                    onFavorites(
                                        db,
                                        navData.uid,
                                        Favorite(clickedBook.key),  // Берем ключ нажатой книги
                                        !clickedBook.isFavorite
                                    )
                                    clickedBook.copy(isFavorite = !clickedBook.isFavorite)
                                }
                                // Иначе, верни ту же книгу
                                else clickedBook
                            }
                        }
                    )
                }
            }
        }
    }
}


private fun onFavorites(
    db: FirebaseFirestore,
    uid: String,
    favorite: Favorite,  // тут будет идентификатор книги, который мы добавили
    isFavorite: Boolean,
) {
    if (isFavorite) {
        db
            .collection("users")
            .document(uid)
            .collection("favorites")
            .document(favorite.key).set(favorite)
    } else {
        db
            .collection("users")
            .document(uid)
            .collection("favorites")
            .document(favorite.key).delete()
    }
}


private fun getAllBooks(
    db: FirebaseFirestore,
    favoritesList: List<String>,
    onBooks: (List<Book>) -> Unit  // Вернет список книг
) {
    db
        .collection("books")
        .get()
        .addOnSuccessListener { task ->
            val books = task.toObjects(Book::class.java).map { book ->
                if (favoritesList.contains(book.key)) {
                    book.copy(isFavorite = true)
                } else {
                    book
                }
            }
            onBooks(books)

        }
        .addOnFailureListener {
            Log.e("MyLog", "Error during getting books: $it")
        }
}


private fun getAllFavoritesBooks(
    db: FirebaseFirestore,
    idsList: List<String>,
    onBooks: (List<Book>) -> Unit  // Вернет список книг
) {
    db
        .collection("books")
        // Получи все книги, учитывая мой список с избранными книгами (их ключами)
        .whereIn(FieldPath.documentId(), idsList)
        .get()
        .addOnSuccessListener { task ->
            val favoritesBooks = task.toObjects(Book::class.java).map { book ->
                if (idsList.contains(book.key)) {
                    book.copy(isFavorite = true)
                } else {
                    book
                }
            }
            onBooks(favoritesBooks)

        }
        .addOnFailureListener {
            Log.e("MyLog", "Error during getting books: $it")
        }
}


private fun getAllFavoritesIds(
    db: FirebaseFirestore,
    uid: String,
    onFavorites: (List<String>) -> Unit  // Вернет список книг
) {
    db
        .collection("users")
        .document(uid)
        .collection("favorites")
        .get()
        .addOnSuccessListener { task ->
            val favorites = task.toObjects(Favorite::class.java)
            val keyList = arrayListOf<String>()
            favorites.forEach {
                keyList.add(it.key)
            }
            onFavorites(keyList)

        }
        .addOnFailureListener {
            Log.e("MyLog", "Error during getting favorites: $it")
        }
}
