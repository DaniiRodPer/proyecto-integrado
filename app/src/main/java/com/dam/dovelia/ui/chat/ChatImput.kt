package com.dam.dovelia.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dam.dovelia.R
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.theme.ProydrpTheme

@Composable
fun ChatInput(
    text: String,
    onChange: (String) -> Unit,
    onSend: (String) -> Unit
) {
    val dimensions = LocalDimensions.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.standard, vertical = dimensions.medium)
            .height(IntrinsicSize.Min), //Keeps all the children with the same height
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.medium)
    ) {
        TextField(
            text, onChange, Modifier
                .weight(1f)
                .fillMaxHeight(),
            placeholder = {
                Text(
                    text = stringResource(R.string.message_placeholder),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            },
            shape = RoundedCornerShape(dimensions.extraHuge),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onSurface
            ),
            singleLine = true
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .clickable { onSend(text) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.send_icon),
                contentDescription = stringResource(R.string.send_description),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(dimensions.bigger)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChatInputPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            ChatInput("", {}, {})
        }
    }
}
