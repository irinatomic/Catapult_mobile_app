import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.catapult.data.ui.BreedUiModel
import com.example.catapult.R

@Composable
fun FetchingData() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorData(errorMessage: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = errorMessage)
    }
}

@Composable
fun NoData() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = stringResource(id = R.string.no_data))
    }
}

@Composable
fun StarRating(value: Int) {
    val starColor: Color = MaterialTheme.colorScheme.tertiary
    val emptyStarColor: Color = Color.Gray

    Row {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (index < value) starColor else emptyStarColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedsPreviewCard(
    breed: BreedUiModel,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        onClick = onClick
    ) {
        Row(modifier = Modifier.height(160.dp)) {
            BreedsPreviewCardImage(imageUrl = breed.imageUrl)

            Column(modifier = Modifier.fillMaxHeight()
                .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
                .padding(vertical = 4.dp)                              // padding inside the column
                .fillMaxWidth()) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(breed.name)
                        }
                    },
                    fontSize = 16.sp,
                    maxLines = 1
                )


                Spacer(modifier = Modifier.height(4.dp)) // Increased vertical space between elements

                Text(
                    text = "${stringResource(id = R.string.breed_origin)}: ${breed.origins.joinToString(", ")}",
                    fontSize = 14.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp)) // Increased vertical space between elements

                Text(
                    text = "Description: ${
                        if (breed.description.length > 100) breed.description.take(100) + "..."
                        else breed.description
                    }",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start ,
                    modifier = Modifier.padding(top = 4.dp, end = 8.dp)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 8.dp, bottom = 8.dp)
                .fillMaxWidth()
                .height(40.dp)
        ) {
            breed.temperament.take(3).forEach { temperament ->
                SuggestionChip(
                    onClick = { /* does nothing */ },
                    label = { Text(temperament, fontSize = 14.sp) }
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Composable
fun BreedsPreviewCardImage(
    imageUrl: String
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(100.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            loading = { CircularProgressIndicator() }
        )
    }
}