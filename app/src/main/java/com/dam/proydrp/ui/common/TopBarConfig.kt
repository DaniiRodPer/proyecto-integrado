package com.dam.proydrp.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

data class TopBarConfig(
    val show: Boolean,
    val title: Int? = null,
    val titleAlignment: TextAlign = TextAlign.Start,
    val showTitle: Boolean = true,
    val leadingIcon: Int? = null,
    val onLeadingClick: (() -> Unit)? = null,
    val trailingIcon: Int? = null,
    val onTrailingClick: (() -> Unit)? = null,
    val customContent: @Composable (() -> Unit)? = null
)