package com.dam.proydrp.ui.screen.editprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.dam.proydrp.R
import com.dam.proydrp.data.model.AccommodationTag
import com.dam.proydrp.data.model.SUPPORTED_CITIES
import com.dam.proydrp.data.model.UserTag
import com.dam.proydrp.ui.common.LocalDimensions
import com.dam.proydrp.ui.components.AnimationComponent
import com.dam.proydrp.ui.components.FloatingContainer
import com.dam.proydrp.ui.components.buttons.PrimaryButton
import com.dam.proydrp.ui.components.images.HorizontalPhotoSelector
import com.dam.proydrp.ui.components.pickers.CheckBoxField
import com.dam.proydrp.ui.components.pickers.DatePickerField
import com.dam.proydrp.ui.components.pickers.DropdownContainer
import com.dam.proydrp.ui.components.pickers.NumberStepField
import com.dam.proydrp.ui.components.text.TextField
import com.dam.proydrp.ui.components.text.Title
import com.dam.proydrp.ui.screen.profile.ProfileContent
import com.dam.proydrp.ui.screen.profile.ProfileState
import com.dam.proydrp.ui.theme.ProydrpTheme
import com.dam.proydrp.ui.utils.getAccommodationTagIcon
import com.dam.proydrp.ui.utils.getAccommodationTagLabel
import com.dam.proydrp.ui.utils.getUserTagLabel
import java.time.LocalDate

data class EditProfileEvents(
    val onBirthDateChange: (LocalDate?) -> Unit,
    val onCityChange: (String) -> Unit,
    val onUserDescriptionChange: (String) -> Unit,
    val onAccommodationDescriptionChange: (String) -> Unit,
    val onRoomsChange: (Int) -> Unit,
    val onBathroomsChange: (Int) -> Unit,
    val onSquareMetersChange: (String) -> Unit,
    val onAccommodationTagChanged: (AccommodationTag) -> Unit,
    val onUserTagChanged: (UserTag) -> Unit,
    val onGoToBack: () -> Unit
)

@Composable
fun EditProfileScreen(
    onGoToBack: () -> Unit
) {

    val viewModel: EditProfileViewModel = hiltViewModel()
    val currentState: EditProfileState = viewModel.state

    val events = EditProfileEvents(
        onBirthDateChange = viewModel::onBirthDayChange,
        onCityChange = viewModel::onCityChange,
        onUserDescriptionChange = viewModel::onUserDescriptionChange,
        onAccommodationDescriptionChange = viewModel::onAccommodationDescriptionChange,
        onRoomsChange = viewModel::onRoomsChange,
        onBathroomsChange = viewModel::onBathroomsChange,
        onSquareMetersChange = viewModel::onSquareMetersChange,
        onAccommodationTagChanged = viewModel::onAccommodationTagChange,
        onUserTagChanged = viewModel::onUserTagChange,
        onGoToBack = onGoToBack
    )

    when (currentState) {
        EditProfileState.Loading -> {
            AnimationComponent(
                lottie = LottieCompositionSpec.RawRes(R.raw.loading_animation),
                text = stringResource(R.string.loading)
            )
        }

        EditProfileState.Error -> {
            AnimationComponent(
                lottie = LottieCompositionSpec.RawRes(R.raw.error_animation),
                loop = false,
                text = stringResource(R.string.no_data_error)
            )
        }

        is EditProfileState.Success -> {
            EditProfileContent(
                state = viewModel.state,
                events = events
            )
        }
    }
}

@Composable
fun EditProfileContent(
    state: EditProfileState,
    events: EditProfileEvents,
) {
    val state = state as EditProfileState.Success

    val canAddMoreTags = state.accommodationTags.size < 9
    val dimensions = LocalDimensions.current

    Column(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = dimensions.big)
        ) {
            Box(
                modifier = Modifier
                    .height(dimensions.huge)
                    .width(dimensions.huge)
                    .clip(RoundedCornerShape(dimensions.giant))
                    .border(
                        width = dimensions.tiny * 2,
                        color = Color.White,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_pic_test),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(Modifier.width(dimensions.extraLarge))
            Title(
                "María Pérez",
                fontSize = 48.sp,
            )
        }

        FloatingContainer(Modifier.weight(1f)) {
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
                    Spacer(Modifier.height(dimensions.large))
                    DatePickerField(
                        label = stringResource(R.string.birth_date),
                        selectedDate = state.birthDate,
                        onDateSelected = { events.onBirthDateChange(it) },
                        icon = painterResource(R.drawable.calendar_icon),
                        defaultShowModal = false
                    )
                    DropdownContainer(
                        label = stringResource(R.string.city),
                        options = SUPPORTED_CITIES,
                        selectedOptions = if (state.city.isEmpty()) emptyList() else listOf(state.city),
                        onOptionClick = { events.onCityChange(it as String) },
                        optionLabel = { it as String },
                        isMultiSelect = false,
                        leadingIcon = painterResource(R.drawable.location_icon)
                    )
                    DropdownContainer(
                        label = stringResource(R.string.user_tags_title),
                        options = UserTag.entries,
                        selectedOptions = state.userTags,
                        onOptionClick = { tag -> events.onUserTagChanged(tag as UserTag) },
                        optionLabel = { tag -> getUserTagLabel(tag as UserTag) },
                        isMultiSelect = true,
                        maxSelection = 3,
                        leadingIcon = painterResource(R.drawable.label_icon)
                    )
                    TextField(
                        text = state.userDescription,
                        onChange = events.onUserDescriptionChange,
                        label = stringResource(R.string.description),
                        singleLine = false
                    )
                    TextField(
                        text = state.accommodationDescription,
                        onChange = events.onAccommodationDescriptionChange,
                        label = stringResource(R.string.accommodationdescription),
                        singleLine = false
                    )
                    HorizontalDivider(
                        thickness = dimensions.tiny,
                        modifier = Modifier.padding(
                            top = dimensions.medium,
                            bottom = dimensions.standard
                        )
                    )
                    HorizontalPhotoSelector(
                        listOf("", "", "", ""),
                        {},
                        Modifier.padding(bottom = dimensions.standard)
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
                    TextField(
                        text = state.squareMeters,
                        icon = painterResource(R.drawable.house_icon),
                        onChange = events.onSquareMetersChange,
                        label = stringResource(R.string.square_meters_title),
                        singleLine = true,
                        trailingText = stringResource(R.string.square_meters),
                        keyboardType = KeyboardType.Number
                    )
                    HorizontalDivider(
                        thickness = dimensions.tiny,
                        modifier = Modifier.padding(
                            top = dimensions.medium,
                            bottom = dimensions.standard
                        )
                    )
                    AccommodationTag.entries.forEach { tag ->
                        val isSelected = state.accommodationTags.contains(tag)

                        CheckBoxField(
                            label = getAccommodationTagLabel(tag),
                            icon = getAccommodationTagIcon(tag),
                            value = state.accommodationTags.contains(tag),
                            enabled = isSelected || canAddMoreTags,
                            onValueChange = {
                                events.onAccommodationTagChanged(tag)
                            }
                        )
                    }
                }
            }
        }
        PrimaryButton(
            text = stringResource(R.string.save),
            onClick = {},
            modifier = Modifier.padding(top = dimensions.standard).fillMaxWidth(0.9f),
            textPadding = dimensions.small,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun EditProfilePreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val state = EditProfileState.Success()
            val events = EditProfileEvents(
                {}, {}, {}, {}, {}, {}, {}, {}, {}, {}
            )
            EditProfileContent(state = state, events = events)
        }
    }
}