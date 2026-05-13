package com.dam.dovelia.ui.components.images

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.dam.dovelia.R
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.theme.ProydrpTheme

/**
 * Componente HorizontalPhotoSelector:
 * Permite gestionar la galería de fotos mediante un listado horizontal con scroll.
 *
 * Tiene un botón para añadir nuevas imagenes siempre si no se ha superado el límite de imágenes del indicador
 *
 * @param items - Lista de fotos (URLs o archivos locales).
 * @param onAddPhotoClick - Evento para abrir el selector de archivos del sistema.
 * @param onDeletePhotoClick - Evento para elminar una foto específica de la lista.
 * @param maxPhotos - Cantidad máxima de imagenes permitidas.
 *
 * @author Daniel Rodríguez Pérez
 * @version 1.0
 */
@Composable
fun HorizontalPhotoSelector(
    items: List<Any>,
    onAddPhotoClick: () -> Unit,
    onDeletePhotoClick: (Any) -> Unit,
    modifier: Modifier = Modifier,
    maxPhotos: Int = 12
) {

    val dimensions = LocalDimensions.current
    val canAddMore = items.size < maxPhotos

    Column(modifier = modifier) {
        Text(
            text = "${stringResource(R.string.accommodation_photos)} (${items.size}/$maxPhotos)",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = dimensions.large, bottom = dimensions.standard)
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = dimensions.standard),
            horizontalArrangement = Arrangement.spacedBy(dimensions.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (canAddMore) {
                item {
                    Box(Modifier.padding(end = dimensions.standard)) {
                        Box(
                            modifier = Modifier
                                .size(dimensions.huge)
                                .clip(RoundedCornerShape(dimensions.large))
                                .background(MaterialTheme.colorScheme.tertiary)
                                .clickable { onAddPhotoClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.add_icon),
                                contentDescription = stringResource(R.string.add_image_desc),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(dimensions.bigger)
                            )
                        }
                    }
                }
            }

            items(items) { item ->
                PhotoItem(
                    model = item,
                    width = dimensions.giant,
                    height = dimensions.giant,
                    borderRadius = dimensions.big,
                    showDelete = true,
                    onDeleteClick = { onDeletePhotoClick(item) }
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HorizontalPhotoSelectorPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.surface
        ) {
            HorizontalPhotoSelector(listOf("", "", "", ""), {}, {})
        }
    }
}