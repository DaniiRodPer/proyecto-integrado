package com.dam.dovelia.ui.screen.filterselection

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dam.dovelia.R
import com.dam.dovelia.data.model.AccommodationTag
import com.dam.dovelia.data.model.CityResult
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.components.pickers.CheckBoxField
import com.dam.dovelia.ui.components.FloatingContainer
import com.dam.dovelia.ui.components.buttons.PrimaryButton
import com.dam.dovelia.ui.components.buttons.SecondaryButton
import com.dam.dovelia.ui.components.pickers.CityAutocompleteField
import com.dam.dovelia.ui.components.pickers.NumberStepField
import com.dam.dovelia.ui.theme.ProydrpTheme
import com.dam.dovelia.ui.utils.getAccommodationTagIcon
import com.dam.dovelia.ui.utils.getAccommodationTagLabel
import kotlin.compareTo

data class FilterSelectionEvents(
    val onCityChange: (String) -> Unit,
    val onRoomsChange: (Int) -> Unit,
    val onBathroomsChange: (Int) -> Unit,
    val onAccommodationTagChanged: (AccommodationTag) -> Unit,
    val onGoToBack: () -> Unit,
    val onSave: (FilterSelectionState) -> Unit,
    val onSearchClick: () -> Unit,
    val onCityQueryChange: (String) -> Unit,
    val onCitySelected: (CityResult) -> Unit,
    val onClearFilters: () -> Unit
)

@Composable
fun FilterSelectionScreen(
    initialCity: String,
    initialRooms: Int,
    initialBathrooms: Int,
    initialTags: List<String>,
    onGoToBack: () -> Unit,
    onSave: (FilterSelectionState) -> Unit
) {
    val viewModel: FilterSelectionViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.onCityQueryChange(initialCity)
        viewModel.onCityChange(initialCity)
        viewModel.onRoomsChange(initialRooms)
        viewModel.onBathroomsChange(initialBathrooms)
        viewModel.onInitialTags(initialTags)
    }

    val events = FilterSelectionEvents(
        onCityChange = viewModel::onCityChange,
        onRoomsChange = viewModel::onRoomsChange,
        onBathroomsChange = viewModel::onBathroomsChange,
        onAccommodationTagChanged = viewModel::onAccommodationTagChange,
        onGoToBack = onGoToBack,
        onSave = onSave,
        onSearchClick = viewModel::searchCities,
        onCitySelected = viewModel::onCitySelected,
        onCityQueryChange = viewModel::onCityQueryChange,
        onClearFilters = viewModel::onClearFilters
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

    val hasActiveFilters = state.city.isNotEmpty() ||
            state.rooms > 1 ||
            state.bathrooms > 1 ||
            state.accommodationTags.isNotEmpty()


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
                    CityAutocompleteField(
                        query = state.citySearchQuery,
                        onQueryChange = events.onCityQueryChange,
                        results = state.citySearchResults,
                        onCitySelect = events.onCitySelected,
                        label = stringResource(R.string.city),
                        leadingIcon = painterResource(R.drawable.location_icon),
                        onSearchClick = events.onSearchClick
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
        Column(
            modifier = Modifier.padding(vertical = dimensions.standard),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PrimaryButton(
                text = stringResource(R.string.save),
                onClick = { events.onSave(state) },
                modifier = Modifier.fillMaxWidth(0.9f),
                textPadding = dimensions.small
            )

            if (hasActiveFilters) {
                Spacer(modifier = Modifier.height(dimensions.small))
                SecondaryButton(
                    text = stringResource(R.string.clear_filters),
                    onClick = events.onClearFilters,
                    modifier = Modifier.fillMaxWidth(0.9f).padding(top = dimensions.small),
                    textPadding = dimensions.little
                )
            }
        }
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
                {}, {}, {}, {}, {}, {},  {}, {}, {}, {}
            )
            FilterSelectionContent(state = state, events = events)
        }
    }
}