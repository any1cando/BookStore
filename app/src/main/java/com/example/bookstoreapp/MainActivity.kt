package com.example.bookstoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.bookstoreapp.ui.login_screen.LoginScreen
import com.example.bookstoreapp.ui.login_screen.data.LoginScreenObject
import com.example.bookstoreapp.ui.login_screen.data.MainScreenDataObject
import com.example.bookstoreapp.ui.main_screen.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            
            NavHost(navController = navController, startDestination = LoginScreenObject) {

                composable<LoginScreenObject> {
                    LoginScreen() { navData ->
                        navController.navigate(navData)

                    }
                }
                // Этот экран будет открываться, когда мы будем отправлять MainScreenDataObject
                composable<MainScreenDataObject> { navBackStackEntry ->
                    val navData = navBackStackEntry.toRoute<MainScreenDataObject>()
                    MainScreen()
                }
            }
        }
    }
}