package com.example.bookstoreapp.ui.main_screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bookstoreapp.dto.Book

@Composable
fun BookItem(book: Book) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {

        var bitMap: Bitmap? = null
        try {
            // Превращаем строку из FirebaseFirestore в бит мап, который принимает AsyncImage
            val base64Image = Base64.decode(book.imageUrl, Base64.DEFAULT)
            bitMap = BitmapFactory.decodeByteArray(base64Image, 0 ,base64Image.size)
        }
        catch (e: IllegalArgumentException) {
            Log.e("MyLog", e.message.toString())
        }

        AsyncImage(
            model = bitMap ?: book.imageUrl,
            contentDescription = "Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = book.name,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = book.description,
            color = Color.Gray,
            fontSize = 16.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = book.price,
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}