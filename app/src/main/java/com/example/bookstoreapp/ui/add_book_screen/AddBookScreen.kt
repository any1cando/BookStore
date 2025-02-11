package com.example.bookstoreapp.ui.add_book_screen

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.bookstoreapp.R
import com.example.bookstoreapp.dto.Book
import com.example.bookstoreapp.ui.login_screen.LoginButton
import com.example.bookstoreapp.ui.login_screen.RoundedCornerTextField
import com.example.bookstoreapp.ui.theme.BoxFilterColor
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


@Composable
fun AddBookScreen(
    onSaved: () -> Unit
) {
    var selectedCategory: String = "BestSellers"
    val titleBook = remember {
        mutableStateOf("")
    }
    val descriptionBook = remember {
        mutableStateOf("")
    }
    val priceBook = remember {
        mutableStateOf("")
    }
    val selectedImageUri = remember {
        mutableStateOf<Uri?>(null)
    }

    val fireStore = remember {
        Firebase.firestore
    }

    val storage = remember {
        Firebase.storage
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri.value = uri

    }

    // Задний фон
    Image(
        painter = rememberAsyncImagePainter(model = selectedImageUri.value),
        contentDescription = "Background",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alpha = 0.4f
    )

    // Придает слегка синий оттенок картинке на заднем фото
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BoxFilterColor)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 45.dp,
                end = 45.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.books_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(90.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Add new book",
            color = Color.White,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )

        Spacer(modifier = Modifier.height(15.dp))

        RoundedCornerDropDownMenu { selectedItem ->
            selectedCategory = selectedItem
        }

        Spacer(modifier = Modifier.height(15.dp))

        RoundedCornerTextField(
            text = titleBook.value,
            label = "Book's title"
        ) {
            titleBook.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            maxLines = 5,
            isSingleLine = false,
            text = descriptionBook.value,
            label = "Book's description"
        ) {
            descriptionBook.value = it
        }

        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            text = priceBook.value,
            label = "Book's price"
        ) {
            priceBook.value = it
        }

        Spacer(modifier = Modifier.height(10.dp))

        LoginButton(text = "Select Image") {
            imageLauncher.launch("image/*")
        }

        LoginButton(text = "Save") {
            saveBookImage(
                selectedImageUri.value!!,
                storage,
                fireStore,
                Book(
                    name = titleBook.value,
                    description = descriptionBook.value,
                    price = priceBook.value,
                    category = selectedCategory
                ),
                onSaved = {
                    onSaved()
                },
                onError = {
                    Log.e("MyLog", "Error during saving book")
                }

            )
        }
    }
}

// Сохраняем сначала картинку, так как для сохранения целой книги нам нужно иметь ссылку
// на эту картинку из storage.
private fun saveBookImage(
    uri: Uri,  // Ссылка картинки на телефоне
    storage: FirebaseStorage,
    fireStore: FirebaseFirestore,
    book: Book,
    onSaved: () -> Unit,
    onError: () -> Unit
) {
    val timeStamp = System.currentTimeMillis()
    val storageRef = storage
        .reference
        .child("book_images")
        .child("image_$timeStamp.jpg")  // Делаем разное название для картинок, чтобы они не сохранялись в одном месте
    val uploadTask = storageRef.putFile(uri)
    uploadTask
        .addOnSuccessListener {
            // Ничего не делаем, пока не получим ссылку картинки
            storageRef.downloadUrl.addOnSuccessListener { url ->  // После получения ссылки - сохранение текстовой части
                saveBookToFireStore(
                    fireStore,
                    url.toString(),
                    book,
                    onSaved = {
                        onSaved()
                    },
                    onError = {
                        onError()
                    }
                )
            }
        }
        .addOnFailureListener {
            Log.e("MyLog", "Error during saving image to storage")
        }
}


private fun saveBookToFireStore(
    fireStore: FirebaseFirestore,
    url: String,
    book: Book,
    onSaved: () -> Unit,
    onError: () -> Unit
) {
    val db = fireStore.collection("books")
    val key = db.document().id
    db.document(key)
        .set(
            book.copy(
                key = key, imageUrl = url
            )
        )
        .addOnSuccessListener {
            onSaved()
        }
        .addOnFailureListener {
            onError()
        }
}

