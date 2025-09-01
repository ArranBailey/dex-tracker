package com.arranbailey.dextracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.arranbailey.dextracker.ui.theme.DextrackerTheme
import com.arranbailey.dextracker.ui.LoadingScreen
import com.arranbailey.dextracker.ui.SetListScreen
import com.arranbailey.dextracker.viewmodel.LoadingViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DextrackerTheme {
                val loadingViewModel: LoadingViewModel = viewModel()
                val isLoading = loadingViewModel.isCaching.value

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isLoading) {
                        LoadingScreen(viewModel = loadingViewModel) {
                            // this will trigger after loading completes
                        }
                    } else {
                        //DexNavHost(rememberNavController())
                        MainApp()
                    }
                }
            }
        }
    }
}





