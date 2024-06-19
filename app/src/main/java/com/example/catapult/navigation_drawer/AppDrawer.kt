package com.example.catapult.navigation_drawer

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

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
fun AppDrawer(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
) {

    Surface(
        modifier = Modifier.fillMaxHeight().width(240.dp),
        color = MaterialTheme.colors.surface
    ) {
        Column(modifier = Modifier.statusBarsPadding()) {
            Text(
                "Catapult",
                modifier = Modifier.padding(14.dp),
                color = MaterialTheme.colors.primary
            )

            Divider(modifier = Modifier.padding(bottom = 4.dp))

            DrawerMenu(navController = navController) {
                scope.launch { drawerState.close() }
            }
        }
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
            DrawerMenuItem(
                iconId = item.icon,
                text = item.title,
                onClick = {
                    closeDrawer()
                    scope.launch {
                        delay(300)
                        navController.navigate(item.route)
                    }
                }
            )
        }
    }
}

@Composable
fun DrawerMenuItem(
    iconId: Int,
    text: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 14.dp)
            .clickable { onClick() }
    ) {
        DrawerMenuItemIcon(iconResId = iconId)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text)
    }
}

@Composable
fun DrawerMenuItemIcon(iconResId: Int) {
    Image(
        painter = painterResource(id = iconResId),
        contentDescription = null,
        modifier = Modifier.size(20.dp)
    )
}

@Preview
@Composable
fun AppDrawerPreview() {
    val mockNavController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    AppDrawer(
        navController = mockNavController,
        drawerState = drawerState,
        scope = rememberCoroutineScope()
    )
}