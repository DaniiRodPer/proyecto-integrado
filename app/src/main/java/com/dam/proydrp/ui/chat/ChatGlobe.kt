package com.dam.proydrp.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview

import com.dam.proydrp.ui.common.LocalDimensions
import com.dam.proydrp.ui.theme.ProydrpTheme

@Composable
fun ChatGlobe(
    text: String,
    time: String,
    sender: Boolean
) {
    val dimensions = LocalDimensions.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensions.standard),
        horizontalArrangement = if (sender) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (sender) {
            Text(
                text = time,
                modifier = Modifier.padding(end = dimensions.small)
            )
            Box(
                Modifier
                    .weight(0.8f, fill = false)
                    .clip(RoundedCornerShape(dimensions.huge))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(dimensions.large)
            ) {
                Text(
                    text,
                    Modifier.padding(horizontal = dimensions.medium),
                    color = MaterialTheme.colorScheme.background,
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(0.8f, fill = false)
                    .border(
                        width = dimensions.little,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(dimensions.huge)
                    )
                    .padding(dimensions.large)
            ) {
                Text(
                    text,
                    Modifier.padding(horizontal = dimensions.medium),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
            Text(
                text = time,
                modifier = Modifier.padding(start = dimensions.small)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChatGlobePreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                ChatGlobe("Hola amiguitos yo me llamo sus alberto", "9:01", false)
                ChatGlobe(
                    "en el video de hoy aprenderemos como aser limonada",
                    "12:28",
                    true
                )
            }
        }
    }
}
