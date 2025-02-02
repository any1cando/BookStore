package com.example.bookstoreapp.ui.login


import android.util.Log
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstoreapp.R
import com.example.bookstoreapp.ui.theme.BoxFilterColor
import com.google.firebase.auth.FirebaseAuth


@Composable
fun LoginScreen() {

    val emailState = remember {
        mutableStateOf("")
    }
    val passwordState = remember {
        mutableStateOf("")
    }


    // Задний фон
    Image(
        painter = painterResource(id = R.drawable.books_background),
        contentDescription = "Background",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    // Придает слегка синий оттенок картинке на заднем фото
    Box(modifier = Modifier
        .fillMaxSize()
        .background(BoxFilterColor))

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
            modifier = Modifier.size(175.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Any1CaNDo Book Store",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(15.dp))

        RoundedCornerTextField(
            text = emailState.value,
            label = "Email"
        ) {
            emailState.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            text = passwordState.value,
            label = "Password"
        ) {
            passwordState.value = it
        }

        Spacer(modifier = Modifier.height(10.dp))
        LoginButton(text = "Sign In!") {
            
        }
        LoginButton(text = "Sign Up!") {

        }
    }
}


private fun signUp(auth: FirebaseAuth, email: String, password: String) {
    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Log.i("SignTest", "Sign Up is successful")
        }
        else {
            Log.i("SignTest", "Sign Up Fail")
        }
    }
}


private fun signIn(auth: FirebaseAuth, email: String, password: String) {
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Log.i("SignTest", "Sign In is successful")
        }
        else {
            Log.i("SignTest", "Sign In Fail")
        }
    }
}
