package com.dam.dovelia.ui.components.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dam.dovelia.R
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.theme.ProydrpTheme

@Composable
fun PasswordField(
    password: String, onChange: (String) -> Unit,
    isError: Boolean = false,
    errorText: String = "",
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var hasInteracted by remember { mutableStateOf(false) }
    val dimensions = LocalDimensions.current

    OutlinedTextField(
        value = password,
        onValueChange = {
            onChange(it)
            hasInteracted = true
        },
        isError = isError,
        label = {
            Text(
                text = stringResource(R.string.password),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        singleLine = true,
        modifier = Modifier
            .widthIn(max = 488.dp)
            .fillMaxWidth(),

        shape = RoundedCornerShape(dimensions.extraHuge),

        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.onBackground,
            unfocusedBorderColor = MaterialTheme.colorScheme.tertiary,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
        ),

        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.password_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(dimensions.big)
            )
        },

        trailingIcon = {
            val visibilityIcon = if (passwordVisible)
                painterResource(R.drawable.visibility_off_icon)
            else painterResource(R.drawable.visibility_on_icon)

            val description = if (passwordVisible)
                stringResource(R.string.password_visibilityOff)
            else stringResource(R.string.password_visibilityOn)

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    painter = visibilityIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(dimensions.big)
                )
            }
        },

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),

        supportingText = {
            if (isError && errorText.isNotEmpty()) {
                Text(
                    text = errorText,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }
        },

        visualTransformation =
            if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation('\u2022')
            }

    )
}

@Preview(showBackground = true)
@Composable
fun PasswordFieldPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            PasswordField("0000000000", onChange = {})
        }
    }
}