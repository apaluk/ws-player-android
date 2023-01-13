package com.apaluk.wsplayer.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.apaluk.wsplayer.R
import com.apaluk.wsplayer.domain.model.media.SearchResultItem
import com.apaluk.wsplayer.ui.theme.WsPlayerTheme
import timber.log.Timber

@Composable
fun SearchResults(
    results: List<SearchResultItem>,
    modifier: Modifier = Modifier,
    onResultClicked: (String) -> Unit,
    scrollToTop: Boolean
) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        items(results) { result ->
            SearchResult(
                item = result,
                onClicked = { onResultClicked(result.id) }
            )
            Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)
        }
    }
    LaunchedEffect(scrollToTop) {
        if(scrollToTop) {
            listState.scrollToItem(0)
        }
    }
}

@Composable
fun SearchResult(
    item: SearchResultItem,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit,
) {
    val imgHeight = 180.dp
    val imgWidth = 120.dp
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(imgHeight)
            .clickable { onClicked() }
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = modifier
                .width(imgWidth)
                .height(imgHeight)
                .background(color = MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            if (item.imageUrl.isNullOrEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_movie_64),
                    contentDescription = null
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.imageUrl)
                        .crossfade(durationMillis = 100)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                Timber.d("xxx title=${item.title} original=${item.originalTitle}")
                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = item.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                val originalTitle = item.originalTitle
                if(originalTitle != null && originalTitle != item.title) {
                    Text(
                        modifier = Modifier.padding(start = 4.dp).alignByBaseline(),
                        text = "($originalTitle)",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                }

            }
            Text(
                modifier = modifier.padding(vertical = 12.dp),
                text = item.year,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = item.cast.take(3).joinToString(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
fun SearchResultsPreview() {
    WsPlayerTheme {
        SearchResult(
            item = SearchResultItem(
                id = "",
                title = "Pulp fiction",
                originalTitle = "Pulp fiction",
                year = "1994",
                duration = 9240,
                genre = listOf("Thriller", "Comedy"),
                cast = listOf("Bruce Willis", "John Travolta", "Samuel L. Jackson"),
                director = listOf("Quentin Tarantino"),
                imageUrl = null
            ),
            onClicked = {}
        )
    }
}