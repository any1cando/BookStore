package com.example.bookstoreapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bookstoreapp.dto.Book
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val fs = Firebase.firestore
    val storage = Firebase.storage.reference.child("images")
    val list = remember {
        mutableStateOf(emptyList<Book>())
    }

    /*** Версия для НЕ динамического чтения коллекции. Один раз что-то добавим - уже не прочитает */

//    fs.collection("test").get().addOnCompleteListener { task ->
//        if (task.isSuccessful) {
//            list.value = task.result.toObjects(Book::class.java)
//        }
//        else {
//            Log.e("Parse Collection Error", task.exception.toString())
//        }
//    }
    val listener = fs.collection(context.getString(R.string.collection_books_name))
        .addSnapshotListener { snapShot, exception ->
            list.value = snapShot?.toObjects(Book::class.java) ?: emptyList()
        }
    /*** Чтобы остановить слушатель, мы делаем "listener.remove()", когда нам понадобится!
     * Это кушает ресурсы, и слушатель надо останавливать!*/

    // Это колонна
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            items(list.value) { book ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = book.imageUrl,
                            contentDescription = "Null",
                            modifier = Modifier.height(100.dp).width(100.dp)
                        )
                        Text(
                            text = book.name, modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth()
                                .padding(5.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            onClick = {
                val task = storage.child("booksTest1.jpg").putBytes(
                    bitmapToByteArray(context = context)
                )
                // Добавляем слушатель: если все прошло хорошо, то будет выведено "Ok"
                task.addOnFailureListener { Log.i("Task 'baos'", "Bad") }
                    .addOnSuccessListener { uploadTask ->
                    Log.i("Task 'baos'", "Ok")
                    // Нам в uploadTask вернулась не ссылка. Ссылку нужно будет еще скачать ->
                    uploadTask.metadata?.reference?.downloadUrl?.addOnCompleteListener { uriTask ->
                        saveBook(fs, uriTask.result.toString())
                    }
                }
            }) {
            Text(
                text = "Add book"
            )
        }
    }
}


private fun bitmapToByteArray(context: Context): ByteArray {
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.books)
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    return baos.toByteArray()
}


private fun saveBook(fs: FirebaseFirestore, imageUrl: String) {
    fs.collection("books")
        .document()
        .set(
            Book(
                "One Piece 2",
                "Insane story about Monkey D. Luffy",
                "550",
                "Adventure",
                imageUrl = imageUrl
            )
        )
}
