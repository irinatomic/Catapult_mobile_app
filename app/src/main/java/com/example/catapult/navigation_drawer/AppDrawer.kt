package com.example.catapult.navigation_drawer

import androidx.compose.material3.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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