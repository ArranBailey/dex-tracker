package com.arranbailey.dextracker

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.arranbailey.dextracker.ui.SetListScreen
import com.arranbailey.dextracker.ui.card.CardDetailsScreen
import com.arranbailey.dextracker.ui.card.CardListScreen

@Composable
fun DexNavHost(navController: NavHostController,modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "sets", modifier = modifier) {
        composable("sets") {
            SetListScreen(onSetClick = { setId ->
                navController.navigate("sets/${setId}")
            })
        }
        composable("search"){
            Text("Search")
        }
        composable("sets/{setId}") { backStackEntry ->
            val setId = backStackEntry.arguments?.getString("setId")
            CardListScreen(onClick = { card ->
                navController.navigate("cards/${card.id}")
            })
            // Display the cards for the given set ID
        }
        composable("cards/{cardId}") { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString("cardId")
            // Display the details of the card with the given ID
            CardDetailsScreen()
        }
    }
}

@Composable
fun TestCards(setId: String){
    Text(setId)
}