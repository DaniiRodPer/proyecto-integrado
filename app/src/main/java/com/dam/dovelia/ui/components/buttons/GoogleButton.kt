package com.dam.dovelia.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.dam.dovelia.R
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.theme.ProydrpTheme

@Composable
fun GoogleButton(
    modifier: Modifier = Modifier,
) {

    val dimensions = LocalDimensions.current

    OutlinedButton(
        onClick = {},
        modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensions.large),
        border = BorderStroke(dimensions.little, MaterialTheme.colorScheme.primary),
        contentPadding = PaddingValues(vertical = dimensions.big, horizontal = dimensions.medium),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            itemVerticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.google_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(dimensions.big),
            )
            Text(
                "Entra con ",
                Modifier.padding(
                    start = dimensions.large,
                ),
                fontSize = 18.sp
            )
            Text(
                "Google",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun GoogleButtonPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.surface
        ) {
            GoogleButton()
        }
    }
}