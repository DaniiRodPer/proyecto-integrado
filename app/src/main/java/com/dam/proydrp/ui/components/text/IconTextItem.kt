package com.dam.proydrp.ui.components.text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.dam.proydrp.data.model.AccommodationTag
import com.dam.proydrp.ui.common.LocalDimensions
import com.dam.proydrp.ui.theme.ProydrpTheme
import com.dam.proydrp.ui.utils.getAccommodationTagIcon
import com.dam.proydrp.ui.utils.getAccommodationTagLabel

@Composable
fun IconTextItem(
    iconResId: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    val dimensions = LocalDimensions.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(dimensions.extraLarge)
        )
        Spacer(modifier = Modifier.width(dimensions.small))
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 12.sp,
            style = MaterialTheme.typography.bodyMedium,
            softWrap = false
        )
    }
}


@Preview
@Composable
fun AccommodationTagItemPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.surface
        ) {
            IconTextItem(
                getAccommodationTagIcon(AccommodationTag.POOL),
                getAccommodationTagLabel(AccommodationTag.POOL)
            )
        }
    }
}