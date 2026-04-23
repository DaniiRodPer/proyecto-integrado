package com.dam.dovelia.ui.components.images

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.tooling.preview.Preview
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.theme.ProydrpTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalGallery(
    urls: List<String>,
    height: Dp,
    modifier: Modifier = Modifier
) {
    val dimensions = LocalDimensions.current

    if (urls.isEmpty()) return

    val calculatedWidth = height * (3f / 4f)

    val infiniteCount = Int.MAX_VALUE
    val initialPage = infiniteCount / 2 - (infiniteCount / 2 % urls.size)

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { infiniteCount }
    )

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val maxWidth = maxWidth
        val horizontalPadding = (maxWidth - calculatedWidth) / 2

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = horizontalPadding),
            pageSpacing = dimensions.standard,
            pageSize = PageSize.Fixed(calculatedWidth),
            verticalAlignment = Alignment.CenterVertically
        ) { page ->
            val index = page % urls.size

            PhotoItem(
                model = urls[index],
                borderRadius = dimensions.big,
                modifier = Modifier
                    .height(height)
                    .aspectRatio(3f / 4f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HorizontalGalleryPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.surface
        ) {
            HorizontalGallery(listOf("", "", "", ""), height = 250.dp)
        }
    }
}