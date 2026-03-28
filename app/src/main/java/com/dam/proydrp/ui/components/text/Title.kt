package com.dam.proydrp.ui.components.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.dam.proydrp.ui.theme.ProydrpTheme

@Composable
fun Title(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 32.sp,
    color: Color = MaterialTheme.colorScheme.primary,
    center: Boolean = false
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge.copy(
            fontSize = fontSize,
            color = color,
        ),
        textAlign = if(center) TextAlign.Center else null
    )
}

@Composable
@Preview
fun TitlePreview(){
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Title("Example")
        }
    }
}