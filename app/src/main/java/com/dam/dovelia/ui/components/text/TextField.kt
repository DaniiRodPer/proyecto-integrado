package com.dam.dovelia.ui.components.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dam.dovelia.R
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.theme.ProydrpTheme

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    text: String,
    onChange: (String) -> Unit,
    label: String,
    icon: Painter? = null,
    isError: Boolean = false,
    errorText: String = "",
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingText: String? = null,
    trailingIcon: Int? = null,
    trailingIconAction: () -> Unit = {},
    maxLength: Int? = null
) {

    val dimensions = LocalDimensions.current

    OutlinedTextField(
        value = text,
        onValueChange = {
            onChange(it)
        },

        maxLines = if (singleLine) 1 else 3,
        readOnly = readOnly,

        label = { Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold) },
        singleLine = singleLine,
        isError = isError,
        modifier = modifier
            .widthIn(min = 48.dp)
            .fillMaxWidth(),

        shape = RoundedCornerShape(dimensions.bigger),

        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.onBackground,
            unfocusedBorderColor = MaterialTheme.colorScheme.tertiary,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
        ),

        leadingIcon = if (icon != null) {
            {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(dimensions.big)
                )
            }
        } else null,

        trailingIcon = if (trailingText != null) {
            {
                Text(
                    trailingText,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = modifier.padding(end = dimensions.large)
                )
            }
        } else if (trailingIcon != null) {
            {
                IconButton(
                    onClick = trailingIconAction,
                    modifier = Modifier.padding(end = dimensions.small),
                    colors = IconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.background,
                        disabledContentColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Icon(
                        painterResource(trailingIcon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }
        } else
            null,

        supportingText = {
            val hasError = isError && errorText.isNotEmpty()

            if (hasError || maxLength != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (hasError) Arrangement.SpaceBetween else Arrangement.End
                ) {
                    if (hasError) {
                        Text(
                            text = errorText,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (maxLength != null) {
                        Text(
                            text = "${text.length} / $maxLength",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = dimensions.small)
                        )
                    }
                }
            }
        },

        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun TextFieldPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            TextField(
                text = "user@mail.com",
                onChange = {},
                label = stringResource(R.string.email_address),
                icon = painterResource(id = R.drawable.email_icon),
                trailingIcon = R.drawable.search_icon
            )
        }
    }
}