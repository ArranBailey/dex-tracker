package com.arranbailey.dextracker

import android.icu.text.CaseMap
import android.widget.SlidingDrawer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    // Top-level tabs you want the bottom bar on
    val topLevelRoutes = setOf("sets", "search")
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in topLevelRoutes
    NavDrawer(drawerState = drawerState, content = {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    })

            },
            bottomBar = {
                if (showBottomBar) {
                    BottomNavBar(
                        navController = navController,
                        items = listOf(
                            BottomItem("sets", "Sets", Icons.Default.Home),
                            BottomItem("search", "Search", Icons.Default.Search)
                        )
                    )
                }
            }
        ) { innerPadding ->
            DexNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    })
}

data class BottomItem(val route: String, val label: String, val icon: ImageVector)

@Composable
fun BottomNavBar(
    navController: NavHostController,
    items: List<BottomItem>
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar(modifier = Modifier.height(60 .dp)) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}


@Composable
fun NavDrawer(drawerState: DrawerState,content: @Composable () -> Unit){
    ModalNavigationDrawer(drawerContent =
        { ModalDrawerSheet{
            Text("Test")
            }
        }, drawerState = drawerState
    ){
        content()
    }
}