package com.example.bookstoreapp.ui.login_screen


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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstoreapp.R
import com.example.bookstoreapp.ui.theme.BoxFilterColor
import com.example.bookstoreapp.ui.theme.ErrorColorBackground
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@Composable
fun LoginScreen() {

    val auth = remember {
        Firebase.auth
    }

    val errorState = remember {
        mutableStateOf("")
    }

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

        if (errorState.value.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .background(
                        color = ErrorColorBackground,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = errorState.value,
                    color = Color.Red,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        LoginButton(text = "Sign In!") {
            signIn(
                auth = auth,
                email = emailState.value,
                password = passwordState.value,
                onSignInSuccess = {
                    Log.e("MyLog", "Sign In Success!")
                    errorState.value = ""
                },
                onSignInFailure = { error ->
                    Log.e("MyLog", "Sign In Failure!")
                    errorState.value = error
                }
            )
        }

        LoginButton(text = "Sign Up!",) {
            signUp(
                auth = auth,
                email = emailState.value,
                password = passwordState.value,
                onSignUpSuccess = {
                    errorState.value = ""
                },
                onSignUpFailure = { error ->
                    errorState.value = error
                }
            )
        }
    }
}


private fun signIn(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onSignInSuccess: () -> Unit,
    onSignInFailure: (String) -> Unit) {
    if (email.isBlank() || password.isBlank()) {
        onSignInFailure("Email or password can't be empty!")
        return
    }

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) { onSignInSuccess() }
        }
        .addOnFailureListener { task ->
            onSignInFailure(task.message ?: "Undefined Sign In Error!")
        }
}


private fun signUp(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onSignUpSuccess: () -> Unit,  // Две функции, которые будут что-то делать. Первая ничего не возвращает и переводит
    onSignUpFailure: (String) -> Unit  // на следующий экран, а вторая вернет какую-то строку с ошибкой
) {
    if (email.isBlank() || password.isBlank()) {
        onSignUpFailure("Email or password can't be empty!")
        return
    }
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) { onSignUpSuccess() }
        }
        .addOnFailureListener { task ->
            onSignUpFailure(task.message ?: "Undefined Sign Up Error!")
        }
}
