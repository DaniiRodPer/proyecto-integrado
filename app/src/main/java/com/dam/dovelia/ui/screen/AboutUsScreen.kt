package com.dam.dovelia.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.dam.dovelia.R
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.components.AboutUsItem
import com.dam.dovelia.ui.components.FloatingContainer
import com.dam.dovelia.ui.components.text.Title
import com.dam.dovelia.ui.theme.ProydrpTheme

/**
 * Función AboutUsScreen:
 * Pantalla informativa que muestra detalles sobre el desarrolador y el proyecto.
 * Incluye el logotipo de la aplicación, una descripción del propósito de la misma y enlaces externos a perfiles de redes.
 *
 * @param modifier
 *
 * @author Daniel Rodríguez Pérez
 * @version 1.0
 */
@Composable
fun AboutUsScreen(
    modifier: Modifier = Modifier,
) {
    val uriHandler =
        LocalUriHandler.current

    val dimensions = LocalDimensions.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensions.large),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_notification),
            contentDescription = null,
            modifier = Modifier.size(dimensions.extraGiant)
        )
        Title(
            text = stringResource(R.string.app_name),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 64.sp
        )

        Text(
            text = stringResource(R.string.app_desc),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )

        Spacer(Modifier.height(dimensions.extraLarge))


        FloatingContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensions.large)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = dimensions.extraLarge),
            ) {
                item {
                    Spacer(Modifier.height(dimensions.extraLarge))
                    Text(
                        text = "\"${stringResource(R.string.abus_desc)}\"",
                        style = TextStyle(
                            fontSize = 16.sp,
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(dimensions.huge))
                }
                item {
                    Text(
                        text = stringResource(R.string.abus_title),
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    HorizontalDivider(
                        thickness = dimensions.tiny,
                        modifier = Modifier.padding(
                            top = dimensions.medium,
                            bottom = dimensions.standard
                        )
                    )

                    Spacer(Modifier.height(dimensions.small))
                }

                item {
                    AboutUsItem(
                        modifier,
                        "Nombre",
                        "Daniel Rodríguez Pérez",
                        painter = painterResource(id = R.drawable.badge_icon)
                    )
                    Spacer(Modifier.height(dimensions.standard))
                }
                item {
                    AboutUsItem(
                        modifier,
                        "Curso",
                        "2º DAM",
                        painter = painterResource(id = R.drawable.grade_icon)
                    )
                    Spacer(Modifier.height(dimensions.standard))
                }
                item {
                    AboutUsItem(
                        modifier,
                        "Centro",
                        "IES Portada Alta, Málaga",
                        painter = painterResource(id = R.drawable.bank_icon)
                    )
                    Spacer(Modifier.height(dimensions.standard))
                    Text(
                        text = stringResource(R.string.abus_proy_info),
                        style = TextStyle(
                            fontSize = 16.sp,
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(dimensions.huge))

                }


                item {
                    Text(
                        text = stringResource(R.string.abus_links_title),
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(dimensions.small))
                }

                item {
                    HorizontalDivider(
                        thickness = dimensions.tiny,
                        modifier = Modifier.padding(
                            bottom = dimensions.standard
                        )
                    )

                    Button(
                        onClick = { uriHandler.openUri("https://github.com/DanielRodPer") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensions.huge)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.github_icon),
                            contentDescription = null,
                            modifier = Modifier.size(dimensions.extraBig),
                            tint = MaterialTheme.colorScheme.background
                        )
                        Spacer(modifier.width(dimensions.large))
                        Text(
                            text = "Github",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                    Spacer(Modifier.height(dimensions.standard))
                }


                item {
                    Button(
                        onClick = { uriHandler.openUri("https://www.linkedin.com/in/danielrodrp/") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensions.huge)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.linkedin_icon),
                            contentDescription = null,
                            modifier = Modifier.size(dimensions.extraBig),
                            tint = MaterialTheme.colorScheme.background
                        )
                        Spacer(modifier.width(dimensions.large))
                        Text(
                            text = "LinkedIn",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                    Spacer(Modifier.height(dimensions.standard))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutUsScreenPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            AboutUsScreen()
        }
    }
}