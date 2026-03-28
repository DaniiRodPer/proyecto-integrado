package com.dam.proydrp.ui.components.pickers

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.dam.proydrp.R
import com.dam.proydrp.ui.common.LocalDimensions
import com.dam.proydrp.ui.theme.ProydrpTheme

@Composable
fun NumberStepField(
    modifier: Modifier = Modifier,
    label: String,
    icon: Int,
    value: Int,
    onValueChange: (Int) -> Unit,
    minValue: Int = 1,
    maxValue: Int = 50,
) {

    val dimensions = LocalDimensions.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painterResource(icon),
            contentDescription = null,
            modifier = Modifier
                .padding(
                    top = dimensions.standard,
                    bottom = dimensions.standard,
                    end = dimensions.standard
                )
                .size(dimensions.bigger),
            tint = MaterialTheme.colorScheme.secondary
        )
        Text(
            label,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp
        )
        NumberSelector(
            modifier = Modifier,
            value = value,
            onValueChange = onValueChange,
            minValue = minValue,
            maxValue = maxValue
        )
    }
}

@Preview
@Composable
fun NumberStepFieldPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            NumberStepField(
                label = stringResource(R.string.bedrooms),
                icon = R.drawable.bed_icon,
                value = 5,
                onValueChange = {})
        }
    }
}