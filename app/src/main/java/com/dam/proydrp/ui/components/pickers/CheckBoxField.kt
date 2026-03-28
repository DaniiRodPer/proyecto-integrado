package com.dam.proydrp.ui.components.pickers

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
fun CheckBoxField(
    label: String,
    icon: Int? = null,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    showCheckBox: Boolean = true
) {

    val dimensions = LocalDimensions.current

    val contentAlpha = if (enabled) 1f else 0.38f
    val iconTint = if (enabled)
        MaterialTheme.colorScheme.secondary
    else
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = if (showCheckBox && enabled) {
            Modifier
                .fillMaxWidth()
                .combinedClickable(onClick = { onValueChange(!value) })
        } else {
            Modifier
                .fillMaxWidth()
        }

    ) {
        if (icon != null) {
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
                tint = iconTint
            )
        }
        Text(
            label,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.primary.copy(alpha = contentAlpha),
            fontSize = 14.sp
        )
        if (showCheckBox) {
            Checkbox(
                checked = value,
                null,
                enabled = enabled,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.tertiary,
                    checkmarkColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    }
}

@Preview
@Composable
fun CheckBoxFieldPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            CheckBoxField(
                label = stringResource(R.string.wifi),
                icon = R.drawable.wifi_icon,
                value = true,
                onValueChange = {})
        }
    }
}