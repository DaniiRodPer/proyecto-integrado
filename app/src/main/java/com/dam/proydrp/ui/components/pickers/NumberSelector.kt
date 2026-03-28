package com.dam.proydrp.ui.components.pickers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.dam.proydrp.R
import com.dam.proydrp.ui.common.LocalDimensions
import com.dam.proydrp.ui.theme.ProydrpTheme

@Composable
fun NumberSelector(
    modifier: Modifier = Modifier,
    value: Int,
    onValueChange: (Int) -> Unit,
    minValue: Int = 1,
    maxValue: Int = 50,
) {

    val dimensions = LocalDimensions.current

    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(dimensions.extraBig)
            )
            .padding(dimensions.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = { onValueChange(value - 1) },
            enabled = value > minValue,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.tertiary,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                disabledContentColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.size(dimensions.big)
        ) {
            Icon(
                painterResource(R.drawable.remove_icon),
                contentDescription = stringResource(R.string.decrease_ammount),
                Modifier.size(dimensions.big)
            )
        }
        Text(
            value.toString(),
            modifier
                .width(dimensions.big)
                .wrapContentWidth(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(
            onClick = { onValueChange(value + 1) },
            enabled = value < maxValue,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.tertiary,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                disabledContentColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.size(dimensions.big)
        ) {
            Icon(
                painterResource(R.drawable.add_icon),
                contentDescription = stringResource(R.string.increase_ammount),
                Modifier.size(dimensions.big)
            )
        }
    }
}

@Preview
@Composable
fun NumberSelectorPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            NumberSelector(value = 5, onValueChange = {})
        }
    }
}