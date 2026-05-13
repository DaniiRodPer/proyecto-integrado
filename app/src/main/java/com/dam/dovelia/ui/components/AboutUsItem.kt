package com.dam.dovelia.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.dam.dovelia.R
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.components.text.Title
import com.dam.dovelia.ui.theme.ProydrpTheme

/**
 * Función composable que muestra datos en el aboutUs
 *
 * @param titulo
 * @param subtitulo
 * @param painter - Icono a mostrar
 *
 * @author: Daniel Rodíguez Pérez
 * @version: 1.0
 */
@Composable
fun AboutUsItem(modifier: Modifier, titulo: String, subtitulo: String, painter: Painter){
    val dimensiones = LocalDimensions.current

    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensiones.medium),
        border = BorderStroke(
            width = dimensiones.tiny * 2,
            color = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = dimensiones.standard, horizontal = dimensiones.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painter,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(dimensiones.huge)
            )

            Spacer(Modifier.width(dimensiones.large))

            Column {
                Title(
                    text = titulo,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = subtitulo,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AboutUsItemPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            AboutUsItem(Modifier, "Dovelia", "Tu app de intercambio de casas", painterResource(R.drawable.app_icon))
        }
    }
}
