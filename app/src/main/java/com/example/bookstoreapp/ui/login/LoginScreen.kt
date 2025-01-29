package com.example.bookstoreapp.ui.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen() {
    val auth = Firebase.auth

    val emailState = remember {
        mutableStateOf("")
    }
    val passwordState = remember {
        mutableStateOf("")
    }
    Log.i("My log", "User email - ${auth.currentUser?.email}")
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = emailState.value, onValueChange = {
            emailState.value = it
        })
        Spacer(modifier = Modifier.height(10.dp))

        TextField(value = passwordState.value, onValueChange = {
            passwordState.value = it
        })
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            signUp(auth, email = emailState.value, password = passwordState.value)
        }) {
            Text(text = "Sign Up!")
        }
        Button(onClick = {
            signIn(auth, email = emailState.value, password = passwordState.value)
        }) {
            Text(text = "Sign In!")
        }
        Button(onClick = {
            signOut(auth)
        }) {
            Text(text = "Sign Out!")
        }
        Button(onClick = {
            deleteAcc(auth, email = emailState.value, password = passwordState.value)
        }) {
            Text(text = "Delete account.")
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


private fun signOut(auth: FirebaseAuth) {
    auth.signOut()
    Log.i("SignTest", "Sign Out is successful!")
}


private fun deleteAcc(auth: FirebaseAuth, email: String, password: String) {
    val credential = EmailAuthProvider.getCredential(email, password)
    auth.currentUser?.reauthenticate(credential)?.addOnCompleteListener { taskReAuth ->
        if (taskReAuth.isSuccessful) {
            auth.currentUser?.delete()?.addOnCompleteListener { taskDelete ->
                if (taskDelete.isSuccessful) Log.i("Delete Tag", "Delete is successful")
                else Log.i("Delete Tag", "Delete is Fail")
            }
        }
        else Log.i("Delete Tag", "Reauthentication is Fail!")
    }
}
