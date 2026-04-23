package com.dam.dovelia.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dam.dovelia.R
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.common.TopBarConfig
import com.dam.dovelia.ui.components.text.Title
import com.dam.dovelia.ui.theme.ProydrpTheme

@Composable
fun CustomizableTopBar(config: TopBarConfig) {

    val dimensions = LocalDimensions.current

    AnimatedContent(
        targetState = config,
        transitionSpec = {
            fadeIn(animationSpec = tween(400)) togetherWith
                    fadeOut(animationSpec = tween(400))
        },
        label = "TopBarContentAnimation"
    ) { targetConfig ->
        Surface(
            color = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = dimensions.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Leading
                Box(Modifier.width(48.dp), contentAlignment = Alignment.Center) {
                    targetConfig.leadingIcon?.let { icon ->
                        IconButton(onClick = { targetConfig.onLeadingClick?.invoke() }) {
                            Icon(
                                painterResource(icon),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(dimensions.big)
                            )
                        }
                    }
                }

                //Content
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    contentAlignment = when (targetConfig.titleAlignment) {
                        TextAlign.Start -> Alignment.CenterStart
                        TextAlign.End -> Alignment.CenterEnd
                        else -> Alignment.Center
                    }
                ) {
                    if (targetConfig.customContent != null) {
                        targetConfig.customContent()
                    } else if (targetConfig.title != null) {
                        Text(
                            text = stringResource(targetConfig.title!!),
                            fontSize = 20.sp,
                            textAlign = targetConfig.titleAlignment,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                //trailing
                Box(Modifier.width(dimensions.huge), contentAlignment = Alignment.Center) {
                    targetConfig.trailingIcon?.let { icon ->
                        IconButton(onClick = { targetConfig.onTrailingClick?.invoke() }) {
                            Icon(
                                painterResource(icon),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(dimensions.big)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomizableTopBarPreviewContent() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            Box {
                CustomizableTopBar(
                    config = TopBarConfig(
                        true,
                        leadingIcon = R.drawable.arrow_back_icon,
                        trailingIcon = R.drawable.sort_icon,
                        titleAlignment = TextAlign.Center,
                        customContent = {
                            Title(
                                stringResource(R.string.discover_title),
                                fontSize = 36.sp
                            )
                        }
                    ))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomizableTopBarPreviewText() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            Box {
                CustomizableTopBar(
                    config = TopBarConfig(
                        true,
                        leadingIcon = R.drawable.arrow_back_icon,
                        trailingIcon = R.drawable.save_icon,
                        title = R.string.edit_profile_title
                    )
                )
            }
        }
    }
}