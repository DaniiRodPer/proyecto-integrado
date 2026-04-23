package com.dam.dovelia.ui.components.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.dam.dovelia.ui.theme.ProydrpTheme

@Composable
fun Title(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 32.sp,
    minFontSize: TextUnit = 24.sp,
    color: Color = MaterialTheme.colorScheme.primary,
    center: Boolean = false,
    maxLines: Int = 2
) {
    var currentFontSize by remember(text) { mutableStateOf(fontSize) }
    var readyToDraw by remember(text) { mutableStateOf(false) }

    Text(
        text = text,
        modifier = modifier.drawWithContent {
            if (readyToDraw) drawContent()
        },
        style = MaterialTheme.typography.titleLarge.copy(
            fontSize = currentFontSize,
            color = color,
        ),
        textAlign = if (center) TextAlign.Center else null,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.hasVisualOverflow && currentFontSize > minFontSize) {
                currentFontSize = (currentFontSize.value - 2).sp
            } else {
                readyToDraw = true
            }
        }
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