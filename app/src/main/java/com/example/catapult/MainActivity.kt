package com.example.catapult

import AppNavigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.catapult.core.theme.CatapultTheme
import com.example.catapult.data.database.CatapultDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CatapultDatabase.initDatabase(context = this)

        setContent {
            CatapultTheme {
               AppNavigation()
            }
        }
    }
}
