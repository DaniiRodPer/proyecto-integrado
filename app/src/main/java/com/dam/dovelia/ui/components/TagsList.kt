package com.dam.dovelia.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dam.dovelia.R
import com.dam.dovelia.data.model.AccommodationTag
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.components.text.IconTextItem
import com.dam.dovelia.ui.theme.ProydrpTheme
import com.dam.dovelia.ui.utils.getAccommodationTagIcon
import com.dam.dovelia.ui.utils.getAccommodationTagLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagList(
    squareM: Int,
    bedrooms: Int,
    bathrooms: Int,
    tags: List<AccommodationTag> = listOf(),
    modifier: Modifier = Modifier,
    multiLine: Boolean = true
) {
    val dimensions = LocalDimensions.current

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalArrangement = Arrangement.spacedBy(dimensions.medium),
        maxItemsInEachRow = 3
    ) {
        val cellModifier = Modifier.weight(1f)
        val itemModifier = Modifier.wrapContentWidth()

        Box(cellModifier, contentAlignment = Alignment.CenterStart) {
            IconTextItem(R.drawable.house_icon, "$squareM m²", itemModifier)
        }

        Box(cellModifier, contentAlignment = Alignment.Center) {
            IconTextItem(
                R.drawable.bed_icon,
                "$bedrooms ${stringResource(R.string.bedrooms)}",
                itemModifier
            )
        }

        Box(cellModifier, contentAlignment = Alignment.CenterEnd) {
            IconTextItem(
                R.drawable.wc_icon,
                "$bathrooms ${stringResource(R.string.bathrooms)}",
                itemModifier
            )
        }

        if (multiLine){
            tags.forEachIndexed { index, tag ->
                val columnPosition = (index + 3) % 3

                val alignment = when (columnPosition) {
                    0 -> Alignment.CenterStart
                    1 -> Alignment.Center
                    else -> Alignment.CenterEnd
                }

                Box(cellModifier, contentAlignment = alignment) {
                    IconTextItem(
                        getAccommodationTagIcon(tag),
                        getAccommodationTagLabel(tag),
                        itemModifier
                    )
                }
            }
        }
        
        val exact = tags.size % 3
        if (exact != 0) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}


@Preview
@Composable
fun TagListPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.surface
        ) {
            TagList(
                120, 3, 2, listOf(
                    AccommodationTag.WIFI,
                    AccommodationTag.CENTER,
                    AccommodationTag.AIR_CONDITIONING,
                    AccommodationTag.SMOKE_FREE,
                    AccommodationTag.SMOKE_FREE
                )
            )
        }
    }
}