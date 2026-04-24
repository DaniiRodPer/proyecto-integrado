package com.dam.dovelia.ui.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.theme.ProydrpTheme

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    loadingText: String? = null,
    textPadding: Dp = LocalDimensions.current.standard
) {

    val dimensions = LocalDimensions.current

    Button(
        onClick = onClick,
        modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensions.large),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.background,
            disabledContainerColor = MaterialTheme.colorScheme.onBackground,
            disabledContentColor = MaterialTheme.colorScheme.background
        ),
        enabled = enabled && !isLoading
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            itemVerticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            if(isLoading){
                CircularProgressIndicator(color = MaterialTheme.colorScheme.background)
                if(loadingText != null){
                    Text(loadingText, Modifier.padding(textPadding), fontSize = 18.sp)
                } else {
                    Text(text, Modifier.padding(textPadding), fontSize = 18.sp)
                }
            } else {
                Text(text, Modifier.padding(textPadding), fontSize = 18.sp)
            }
        }
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            PrimaryButton("Login", {}, isLoading = false)
        }
    }
}