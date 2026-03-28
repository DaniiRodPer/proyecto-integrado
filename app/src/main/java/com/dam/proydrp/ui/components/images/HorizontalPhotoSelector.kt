package com.dam.proydrp.ui.components.images

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.dam.proydrp.R
import com.dam.proydrp.ui.common.LocalDimensions
import com.dam.proydrp.ui.theme.ProydrpTheme

@Composable
fun HorizontalPhotoSelector(
    urls: List<String>,
    onAddPhotoClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val dimensions = LocalDimensions.current

    Column(modifier = modifier) {
        Text(
            stringResource(R.string.accommodation_photos),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = dimensions.large, bottom = dimensions.standard)
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = dimensions.standard),
            horizontalArrangement = Arrangement.spacedBy(dimensions.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                Box(Modifier.padding(end = dimensions.standard)) {
                    Box(
                        modifier = Modifier
                            .size(dimensions.huge)
                            .clip(RoundedCornerShape(dimensions.large))
                            .background(MaterialTheme.colorScheme.tertiary)
                            .clickable { onAddPhotoClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.add_icon),
                            contentDescription = stringResource(R.string.add_image_desc),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(dimensions.bigger)
                        )
                    }
                }
            }

            items(urls) { url ->
                PhotoItem(
                    url = url,
                    width = dimensions.giant,
                    height = dimensions.giant,
                    borderRadius = dimensions.big
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HorizontalPhotoSelectorPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.surface
        ) {
            HorizontalPhotoSelector(listOf("", "", "", ""), {})
        }
    }
}