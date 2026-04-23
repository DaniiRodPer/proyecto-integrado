package com.dam.dovelia.ui.components.pickers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dam.dovelia.R
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.components.text.TextField
import com.dam.dovelia.ui.theme.ProydrpTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownContainer(
    label: String,
    options: List<Any>,
    selectedOptions: List<Any>,
    onOptionClick: (Any) -> Unit,
    optionLabel: @Composable (Any) -> String,
    modifier: Modifier = Modifier,
    isMultiSelect: Boolean = false,
    maxSelection: Int = Int.MAX_VALUE,
    leadingIcon: Painter? = null,
    isError: Boolean = false,
    errorText: String = ""
) {
    val dimensions = LocalDimensions.current

    var expanded by remember { mutableStateOf(false) }

    val displayText = when {
        selectedOptions.isEmpty() -> stringResource(R.string.user_tag_selector_title)
        isMultiSelect -> {
            val labels = selectedOptions.map { optionLabel(it) }
            labels.joinToString(", ")
        }

        else -> optionLabel(selectedOptions.first())
    }

    Column(modifier = modifier.fillMaxWidth()) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                text = displayText,
                onChange = {},
                label = label,
                icon = leadingIcon,
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                isError = isError,
                errorText = errorText
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                shape = RoundedCornerShape(dimensions.big),
                containerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(dimensions.big)
                    )
                    .clip(RoundedCornerShape(dimensions.big))
            ) {
                options.forEach { option ->
                    val isSelected = selectedOptions.contains(option)
                    val canSelectMore = selectedOptions.size < maxSelection

                    DropdownMenuItem(
                        text = {
                            val isSelected = isSelected
                            val canSelectMore = canSelectMore
                            val isEnabled = isSelected || canSelectMore || !isMultiSelect

                            CheckBoxField(
                                label = optionLabel(option),
                                value = isSelected,
                                onValueChange = { onOptionClick(option) },
                                enabled = isEnabled,
                                showCheckBox = isMultiSelect
                            )
                        },
                        onClick = {
                            if (!isMultiSelect) {
                                onOptionClick(option)
                                expanded = false
                            } else if (isSelected || canSelectMore) {
                                onOptionClick(option)
                            }
                        },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DropdownContainerPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val options = listOf("Málaga", "Sevilla", "Granada", "Córdoba")
            val selectedOptions = listOf("Málaga")

            DropdownContainer(
                label = stringResource(R.string.city),
                options = options,
                selectedOptions = selectedOptions,
                onOptionClick = {},
                optionLabel = { it as String },
            )
        }
    }
}