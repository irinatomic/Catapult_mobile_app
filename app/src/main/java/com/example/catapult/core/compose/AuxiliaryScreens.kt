package com.example.catapult.core.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.catapult.R

@Composable
fun LoadingScreen() {
    AuxiliaryScreen(
        imageId = R.drawable.ic_hold_on,
        description = "Hold on, you're almost there!"
    )
}

@Composable
fun NoDataScreen() {
    AuxiliaryScreen(
        imageId = R.drawable.ic_no_data,
        description = "Oh no, there's no data to show!"
    )
}

@Composable
private fun AuxiliaryScreen(
    imageId: Int,
    description: String
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = "Loading image",
                modifier = Modifier.size(250.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = description,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

