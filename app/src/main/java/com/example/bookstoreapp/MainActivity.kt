package com.example.bookstoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.bookstoreapp.ui.add_book_screen.AddBookScreen
import com.example.bookstoreapp.ui.add_book_screen.data.AddScreenObject
import com.example.bookstoreapp.ui.add_book_screen.viewmodel.BookViewModel
import com.example.bookstoreapp.ui.login_screen.LoginScreen
import com.example.bookstoreapp.ui.login_screen.data.LoginScreenObject
import com.example.bookstoreapp.ui.login_screen.data.MainScreenDataObject
import com.example.bookstoreapp.ui.main_screen.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val bookViewModel: BookViewModel = viewModel()

            NavHost(navController = navController, startDestination = LoginScreenObject) {

                composable<LoginScreenObject> {
                    LoginScreen { navData ->
                        navController.navigate(navData)

                    }
                }

                // Этот экран будет открываться, когда мы будем отправлять MainScreenDataObject
                composable<MainScreenDataObject> { navBackStackEntry ->
                    val navData = navBackStackEntry.toRoute<MainScreenDataObject>()
                    MainScreen(
                        navData,
                        onEditBookClick = { clickedBook ->
                            bookViewModel.currentBook.value = clickedBook
                            navController.navigate(AddScreenObject)
                        },
                        onAdminClick = {
                            navController.navigate(AddScreenObject)
                        })
                }

                composable<AddScreenObject> { navEntry ->
                    AddBookScreen(bookViewModel = bookViewModel) {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}