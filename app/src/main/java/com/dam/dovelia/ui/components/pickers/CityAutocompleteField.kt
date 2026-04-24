package com.dam.dovelia.ui.components.pickers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.dam.dovelia.R
import com.dam.dovelia.data.model.CityResult
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.components.text.TextField
import com.dam.dovelia.ui.theme.ProydrpTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityAutocompleteField(
    query: String,
    onQueryChange: (String) -> Unit,
    results: List<CityResult>,
    onCitySelect: (CityResult) -> Unit,
    onSearchClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorText: String = "",
    leadingIcon: Painter? = null,
) {
    val dimensions = LocalDimensions.current
    val expanded = results.isNotEmpty()

    Column(modifier = modifier.fillMaxWidth()) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { }
        ) {

            TextField(
                text = query,
                onChange = onQueryChange,
                label = label,
                icon = leadingIcon,
                readOnly = false,
                singleLine = true,
                isError = isError,
                errorText = errorText,
                trailingIcon = R.drawable.search_icon,
                trailingIconAction = onSearchClick,
                modifier = Modifier
                    .menuAnchor()
                    .weight(1f),
            )
            
            if (expanded) {
                ExposedDropdownMenu(
                    expanded = true,
                    onDismissRequest = { onCitySelect(CityResult(-1, query, "", "")) },                    shape = RoundedCornerShape(dimensions.big),
                    containerColor = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(dimensions.big)
                        )
                        .clip(RoundedCornerShape(dimensions.big))
                ) {
                    results.forEach { city ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = city.name,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = listOfNotNull(city.admin1, city.country).joinToString(", "),
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            },
                            onClick = {
                                onCitySelect(city)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CityAutocompleteFieldPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            CityAutocompleteField(
                label = stringResource(R.string.city),
                query = "",
                onQueryChange = {},
                results = listOf(),
                onCitySelect = {},
                onSearchClick = {},
            )
        }
    }
}