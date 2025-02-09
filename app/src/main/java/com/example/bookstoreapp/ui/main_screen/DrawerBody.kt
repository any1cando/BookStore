package com.example.bookstoreapp.ui.main_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstoreapp.R
import com.example.bookstoreapp.ui.theme.DarkTransparentBlue
import com.example.bookstoreapp.ui.theme.DrawerBodyColor
import com.example.bookstoreapp.ui.theme.GrayLine
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@Composable
@Preview
fun DrawerBody() {
    val categoriesList = listOf(
        "Favorites", "Fantasy", "Drama", "Bestsellers"
    )
    val isAdminState = remember {
        mutableStateOf(false)
    }

    // При старте DrawerBody проверяем, админ зашел или нет
    LaunchedEffect(Unit) {
        isAdmin { adminState ->
            isAdminState.value = adminState
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DrawerBodyColor)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.books_background),
            contentDescription = "Navigation Drawer Background",
            alpha = 0.3f,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Categories",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(15.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(GrayLine)
            )
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(categoriesList) { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { },
                    ) {
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = item,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth()
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(GrayLine)
                        )
                    }
                }
            }
            if (isAdminState.value) {
                // Показываем кнопку админа или нет, в зависимости от того, совпал ли uid пользователя
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(64.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkTransparentBlue
                    )
                ) {
                    Text(
                        text = "Admin Button",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// Проверка, зашел админ или нет
fun isAdmin(onAdmin: (Boolean) -> Unit) {
    val currentUid = Firebase.auth.currentUser!!.uid
    Firebase.firestore
        .collection("admin")
        .document(currentUid)
        .get().addOnSuccessListener { adminFirebaseDocumentState -> // эта лямбда - документ в коллекции admin на Firebase Database
            onAdmin(adminFirebaseDocumentState.get("isAdmin") as Boolean)
        }
}

