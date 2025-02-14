package com.example.bookstoreapp.ui.add_book_screen

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.bookstoreapp.R
import com.example.bookstoreapp.dto.Book
import com.example.bookstoreapp.ui.add_book_screen.data.AddScreenObject
import com.example.bookstoreapp.ui.login_screen.LoginButton
import com.example.bookstoreapp.ui.login_screen.RoundedCornerTextField
import com.example.bookstoreapp.ui.theme.BoxFilterColor
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


@Composable
fun AddBookScreen(
    navData: AddScreenObject = AddScreenObject(),  //  Изначально приходят пустые данные
    onSaved: () -> Unit
) {
    val cv = LocalContext.current.contentResolver
    var selectedCategory = navData.category
    val titleBook = remember {
        mutableStateOf(navData.name)
    }
    val descriptionBook = remember {
        mutableStateOf(navData.description)
    }
    val priceBook = remember {
        mutableStateOf(navData.price)
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
            saveBookToFireStore(
                fireStore,
                Book(
                    name = titleBook.value,
                    description = descriptionBook.value,
                    price = priceBook.value,
                    category = selectedCategory,
                    imageUrl = imageToBase64(selectedImageUri.value!!, cv)
                ),
                onSaved = {
                    onSaved()
                },
                onError = {

                }
            )
        }
    }
}

private fun imageToBase64(
    uri: Uri,
    contentResolver: ContentResolver
): String {
    val compressBytes = compressImage(uri, contentResolver)
    return Base64.encodeToString(compressBytes, Base64.DEFAULT)
}


// Функция, которая режет качество, чтобы картинка уместилась в виде строки в FirebaseFirestore
private fun compressImage(uri: Uri, contentResolver: ContentResolver, maxSizeKb: Int = 800): ByteArray {
    val inputStream = contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    var quality = 100
    var byteArrayOutputStream = ByteArrayOutputStream()

    do {
        byteArrayOutputStream.reset()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        quality -= 5
    } while (byteArrayOutputStream.toByteArray().size / 1024 > maxSizeKb && quality > 10)

    return byteArrayOutputStream.toByteArray()
}


private fun saveBookToFireStore(
    fireStore: FirebaseFirestore,
    book: Book,
    onSaved: () -> Unit,
    onError: () -> Unit
) {
    val db = fireStore.collection("books")
    val key = db.document().id
    db.document(key)
        .set(
            book.copy(key = key)
        )
        .addOnSuccessListener {
            onSaved()
        }
        .addOnFailureListener {
            onError()
        }
}

