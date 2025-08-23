package com.arranbailey.dextracker

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.arranbailey.dextracker.ui.SetListScreen
import com.arranbailey.dextracker.ui.card.CardListScreen

@Composable
fun DexNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "sets") {
        composable("sets") {
            SetListScreen(onSetClick = { setId ->
                navController.navigate("cards/${setId}")
            })
        }
        composable("cards/{setId}") { backStackEntry ->
            val setId = backStackEntry.arguments?.getString("setId")
            CardListScreen()
            // Display the cards for the given set ID
        }
    }
}

@Composable
fun TestCards(setId: String){
    Text(setId)
}