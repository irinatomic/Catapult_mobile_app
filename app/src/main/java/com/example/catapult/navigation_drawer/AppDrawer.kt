package com.example.catapult.navigation_drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HamburgerMenu(
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()
    IconButton(
        onClick = {
            scope.launch { drawerState.open() }
        },
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
    }
}

@Composable
fun DrawerMenu(
    navController: NavController,
    closeDrawer: () -> Unit
) {
    val scope = rememberCoroutineScope()
    Column {
        DrawerItem.allItems.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.title) },
                selected = false,
                onClick = {
                    closeDrawer()
                    scope.launch {
                        delay(200)                          // 200 ms delay to allow the drawer to close before navigating
                        navController.navigate(item.route)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AppDrawer(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
) {
    Box(
        Modifier
            .fillMaxHeight()
            .width(240.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column {
            androidx.compose.material3.Text(
                "Catapult",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Divider()
            DrawerMenu(navController = navController) {
                scope.launch { drawerState.close() }
            }
        }
    }
}