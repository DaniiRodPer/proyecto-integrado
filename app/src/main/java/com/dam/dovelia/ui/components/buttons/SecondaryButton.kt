package com.dam.dovelia.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.theme.ProydrpTheme

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textPadding: Dp = LocalDimensions.current.standard
) {
    val dimensions = LocalDimensions.current

    OutlinedButton(
        onClick = onClick,
        modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensions.large),
        border = BorderStroke(dimensions.little, MaterialTheme.colorScheme.primary),
        contentPadding = PaddingValues(vertical = dimensions.standard, horizontal = dimensions.medium),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.tertiary,
        ),
        enabled = enabled
    ) {
        Text(
            text,
            fontSize = 18.sp,
            modifier = Modifier.padding(textPadding),
        )
    }
}

@Preview
@Composable
fun SecondaryButtonPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.surface
        ) {
            SecondaryButton("Cancelar", {})
        }
    }
}