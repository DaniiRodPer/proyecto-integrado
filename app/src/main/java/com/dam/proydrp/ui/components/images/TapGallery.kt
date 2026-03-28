package com.dam.proydrp.ui.components.images

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.dam.proydrp.R
import com.dam.proydrp.ui.common.LocalDimensions
import com.dam.proydrp.ui.theme.ProydrpTheme
import kotlinx.coroutines.launch

@Composable
fun Indicator(isActive: Boolean, modifier: Modifier) {
    val dimensions = LocalDimensions.current

    Box(
        modifier = modifier
            .height(dimensions.small)
            .clip(CircleShape)
            .background(if (isActive) Color.White else Color.White.copy(alpha = 0.3f))
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TapGallery(
    urls: List<String>,
    modifier: Modifier = Modifier,
    city: String
) {
    val pagerState = rememberPagerState(pageCount = { urls.size })
    val scope = rememberCoroutineScope()
    val dimensions = LocalDimensions.current
    val context = LocalContext.current

    LaunchedEffect(urls) {
        urls.forEach { url ->
            val request = ImageRequest.Builder(context)
                .data(url)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build()
            context.imageLoader.enqueue(request)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(3f / 4f)
            .clip(RoundedCornerShape(dimensions.big))
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = false
        ) { page ->
            AsyncImage(
                model = urls[page],
                placeholder = painterResource(R.drawable.image_placeholder),
                error = painterResource(R.drawable.image_placeholder),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensions.standard,
                    start = dimensions.extraLarge,
                    end = dimensions.extraLarge
                ),
            horizontalArrangement = Arrangement.spacedBy(dimensions.small)
        ) {
            urls.forEachIndexed { i, _ ->
                Indicator(
                    modifier = Modifier.weight(1f),
                    isActive = i <= pagerState.currentPage
                )
            }
        }

        FlowRow(
            Modifier
                .align(Alignment.BottomStart)
                .padding(dimensions.standard),
            itemVerticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.location_icon),
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null,
                modifier = Modifier
                    .size(dimensions.extraBig)
            )
            Text(
                text = city,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = dimensions.standard)
            )
        }

        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        scope.launch {
                            val prevPage = if (pagerState.currentPage > 0) {
                                pagerState.currentPage - 1
                            } else {
                                pagerState.currentPage
                            }
                            pagerState.scrollToPage(prevPage)
                        }
                    }
            )


            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        scope.launch {
                            if (pagerState.currentPage < urls.size - 1) {
                                pagerState.scrollToPage(pagerState.currentPage + 1)
                            } else {
                                pagerState.scrollToPage(0)
                            }
                        }
                    }
            )
        }
    }
}

@Preview
@Composable
fun TapGalleryPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.surface
        ) {
            TapGallery(listOf("", "", ""), city = "Ronda, Málaga")
        }
    }
}