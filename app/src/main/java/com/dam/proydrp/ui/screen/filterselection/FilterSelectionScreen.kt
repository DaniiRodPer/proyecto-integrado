package com.dam.proydrp.ui.screen.filterselection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dam.proydrp.R
import com.dam.proydrp.data.model.AccommodationTag
import com.dam.proydrp.data.model.SUPPORTED_CITIES
import com.dam.proydrp.ui.common.LocalDimensions
import com.dam.proydrp.ui.components.pickers.CheckBoxField
import com.dam.proydrp.ui.components.pickers.DropdownContainer
import com.dam.proydrp.ui.components.FloatingContainer
import com.dam.proydrp.ui.components.buttons.PrimaryButton
import com.dam.proydrp.ui.components.pickers.NumberStepField
import com.dam.proydrp.ui.theme.ProydrpTheme
import com.dam.proydrp.ui.utils.getAccommodationTagIcon
import com.dam.proydrp.ui.utils.getAccommodationTagLabel

data class FilterSelectionEvents(
    val onCityChange: (String) -> Unit,
    val onRoomsChange: (Int) -> Unit,
    val onBathroomsChange: (Int) -> Unit,
    val onAccommodationTagChanged: (AccommodationTag) -> Unit,
    val onGoToBack: () -> Unit
)

@Composable
fun FilterSelectionScreen(
    onGoToBack: () -> Unit
) {

    val viewModel: FilterSelectionViewModel = hiltViewModel()

    val events = FilterSelectionEvents(
        onCityChange = viewModel::onCityChange,
        onRoomsChange = viewModel::onRoomsChange,
        onBathroomsChange = viewModel::onBathroomsChange,
        onAccommodationTagChanged = viewModel::onAccommodationTagChange,
        onGoToBack = onGoToBack
    )

    FilterSelectionContent(
        state = viewModel.state,
        events = events
    )
}

@Composable
fun FilterSelectionContent(
    state: FilterSelectionState,
    events: FilterSelectionEvents,
) {
    val dimensions = LocalDimensions.current

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FloatingContainer(
            Modifier
                .weight(1f)
                .padding(top = dimensions.standard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(dimensions.extraBig))
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensions.large)
                ) {
                    Spacer(Modifier.height(dimensions.standard))
                    DropdownContainer(
                        label = stringResource(R.string.city),
                        options = SUPPORTED_CITIES,
                        selectedOptions = if (state.city.isEmpty()) emptyList() else listOf(state.city),
                        onOptionClick = { events.onCityChange(it as String) },
                        optionLabel = { it as String },
                        isMultiSelect = false,
                        leadingIcon = painterResource(R.drawable.location_icon)
                    )
                    HorizontalDivider(
                        thickness = dimensions.tiny,
                        modifier = Modifier.padding(
                            top = dimensions.medium,
                            bottom = dimensions.standard
                        )
                    )
                    NumberStepField(
                        label = stringResource(R.string.bedrooms),
                        icon = R.drawable.bed_icon,
                        value = state.rooms,
                        onValueChange = events.onRoomsChange
                    )
                    NumberStepField(
                        label = stringResource(R.string.bathrooms),
                        icon = R.drawable.wc_icon,
                        value = state.bathrooms,
                        onValueChange = events.onBathroomsChange
                    )
                    HorizontalDivider(
                        thickness = dimensions.tiny,
                        modifier = Modifier.padding(
                            top = dimensions.medium,
                            bottom = dimensions.standard
                        )
                    )
                    AccommodationTag.entries.forEach { tag ->
                        CheckBoxField(
                            label = getAccommodationTagLabel(tag),
                            icon = getAccommodationTagIcon(tag),
                            value = state.accommodationTags.contains(tag),
                            onValueChange = {
                                events.onAccommodationTagChanged(tag)
                            },
                        )
                    }
                }
            }
        }
        PrimaryButton(
            text = stringResource(R.string.save),
            onClick = {},
            modifier = Modifier.padding(top = dimensions.standard).fillMaxWidth(0.9f),
            textPadding = dimensions.small
        )
    }
}


@Preview(showBackground = true)
@Composable
fun FilterSelectionPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val state = FilterSelectionState()
            val events = FilterSelectionEvents(
                {}, {}, {}, {}, {}
            )
            FilterSelectionContent(state = state, events = events)
        }
    }
}