package com.dam.dovelia.ui.screen.editprofile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dam.dovelia.R
import com.dam.dovelia.data.model.AccommodationTag
import com.dam.dovelia.data.model.CityResult
import com.dam.dovelia.data.model.UserProfile
import com.dam.dovelia.data.model.UserTag
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.components.AnimationComponent
import com.dam.dovelia.ui.components.CustomAlertDialog
import com.dam.dovelia.ui.components.FloatingContainer
import com.dam.dovelia.ui.components.buttons.PrimaryButton
import com.dam.dovelia.ui.components.images.HorizontalPhotoSelector
import com.dam.dovelia.ui.components.images.ProfilePic
import com.dam.dovelia.ui.components.pickers.CheckBoxField
import com.dam.dovelia.ui.components.pickers.CityAutocompleteField
import com.dam.dovelia.ui.components.pickers.DatePickerField
import com.dam.dovelia.ui.components.pickers.DropdownContainer
import com.dam.dovelia.ui.components.pickers.NumberStepField
import com.dam.dovelia.ui.components.text.TextField
import com.dam.dovelia.ui.components.text.Title
import com.dam.dovelia.ui.home.DialogConfig
import com.dam.dovelia.ui.theme.ProydrpTheme
import com.dam.dovelia.ui.utils.getAccommodationTagIcon
import com.dam.dovelia.ui.utils.getAccommodationTagLabel
import com.dam.dovelia.ui.utils.getUserTagLabel
import com.dam.dovelia.ui.utils.uriToFile
import java.io.File
import java.time.LocalDate

data class EditProfileEvents(
    val onBirthDateChange: (LocalDate?) -> Unit,
    val onCityQueryChange: (String) -> Unit,
    val onSearchCityClick: () -> Unit,
    val onCitySelected: (CityResult) -> Unit,
    val onUserDescriptionChange: (String) -> Unit,
    val onAccommodationDescriptionChange: (String) -> Unit,
    val onRoomsChange: (Int) -> Unit,
    val onBathroomsChange: (Int) -> Unit,
    val onSquareMetersChange: (String) -> Unit,
    val onAccommodationTagChanged: (AccommodationTag) -> Unit,
    val onUserTagChanged: (UserTag) -> Unit,
    val onAddAccommodationPhoto: (File) -> Unit,
    val onDeleteAccommodationPhoto: (Any) -> Unit,
    val onProfilePhotoChanged: (File) -> Unit,
    val onGoToBack: () -> Unit
)

@Composable
fun EditProfileScreen(
    userData: UserProfile? = null,
    isRegisterMode: Boolean = false,
    onGoToBack: () -> Unit,
    onRegisterSuccess: () -> Unit = {}
) {

    val viewModel: EditProfileViewModel = hiltViewModel()

    LaunchedEffect(userData, isRegisterMode) {
        viewModel.start(userData, isRegisterMode)
    }

    val currentState: EditProfileState = viewModel.state
    val successState = currentState as? EditProfileState.Success
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 12),
        onResult = { uris ->
            if (successState != null) {
                val currentCount =
                    currentState.accommodationPicsUrls.count { it !in successState.pendingDeletions } + successState.pendingUploads.size

                val slotsLeft = 12 - currentCount

                uris.take(slotsLeft).forEach { uri ->
                    val file = uriToFile(context, uri)
                    if (file != null) {
                        viewModel.onAddAccommodationPhoto(file)
                    }
                }
            }
        }
    )

    val profilePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                val file = uriToFile(context, it)
                if (file != null) {
                    viewModel.onProfilePhotoChanged(file)
                }
            }
        }
    )

    val events = EditProfileEvents(
        onBirthDateChange = viewModel::onBirthDayChange,
        onCityQueryChange = viewModel::onCityQueryChange,
        onSearchCityClick = viewModel::searchCities,
        onCitySelected = viewModel::onCitySelected,
        onUserDescriptionChange = viewModel::onUserDescriptionChange,
        onAccommodationDescriptionChange = viewModel::onAccommodationDescriptionChange,
        onRoomsChange = viewModel::onRoomsChange,
        onBathroomsChange = viewModel::onBathroomsChange,
        onSquareMetersChange = viewModel::onSquareMetersChange,
        onAccommodationTagChanged = viewModel::onAccommodationTagChange,
        onUserTagChanged = viewModel::onUserTagChange,
        onAddAccommodationPhoto = viewModel::onAddAccommodationPhoto,
        onDeleteAccommodationPhoto = viewModel::onDeleteAccommodationPhoto,
        onProfilePhotoChanged = viewModel::onProfilePhotoChanged,
        onGoToBack = onGoToBack
    )

    when (currentState) {
        EditProfileState.Loading -> {
            AnimationComponent(
                lottie = R.raw.loading_animation,
                text = stringResource(R.string.loading)
            )
        }

        EditProfileState.Error -> {
            AnimationComponent(
                lottie = R.raw.error_animation,
                loop = false,
                text = stringResource(R.string.no_data_error)
            )
        }

        is EditProfileState.Success -> {
            EditProfileContent(
                state = currentState,
                events = events,
                isRegisterMode = isRegisterMode,
                onSave = {
                    viewModel.onSave {
                        if (isRegisterMode) {
                            onRegisterSuccess()
                        } else {
                            onGoToBack()
                        }
                    }
                },
                onAddPhotoClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly,
                        )
                    )
                },
                onProfilePhotoClick = {
                    profilePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )
        }
    }
}


@Composable
fun EditProfileContent(
    state: EditProfileState,
    events: EditProfileEvents,
    isRegisterMode: Boolean,
    onAddPhotoClick: () -> Unit,
    onProfilePhotoClick: () -> Unit,
    onSave: () -> Unit
) {
    val state = state as EditProfileState.Success

    val canAddMoreTags = state.accommodationTags.size < 9
    val dimensions = LocalDimensions.current
    var activeDialog by remember { mutableStateOf<DialogConfig?>(null) }
    var itemToDelete by remember { mutableStateOf<Any?>(null) }
    val focusManager = LocalFocusManager.current

    Column(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensions.big,
                    horizontal = dimensions.large
                )
        ) {
            val profilePicModel =
                state.pendingProfilePicUpload ?: state.userPicUrl.takeIf { it.isNotBlank() }

            ProfilePic(
                model = profilePicModel,
                size = dimensions.extraHuge,
                clickable = true,
                onClick = onProfilePhotoClick
            )
            Spacer(Modifier.width(dimensions.extraLarge))
            Title(
                text = "${state.name} ${state.surname}",
                fontSize = 48.sp,
                modifier = Modifier.weight(1f, fill = false)
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
                        defaultShowModal = false,
                        isError = state.birthDateIsError,
                        errorText = state.birthDateError?.let { stringResource(it) } ?: ""
                    )
                    CityAutocompleteField(
                        query = state.citySearchQuery,
                        onQueryChange = events.onCityQueryChange,
                        results = state.citySearchResults,
                        onCitySelect = events.onCitySelected,
                        onSearchClick = events.onSearchCityClick,
                        label = stringResource(R.string.city),
                        leadingIcon = painterResource(R.drawable.location_icon),
                        isError = state.cityIsError,
                        errorText = state.cityError?.let { stringResource(it) } ?: ""
                    )
                    DropdownContainer(
                        label = stringResource(R.string.user_tags_title),
                        options = UserTag.entries,
                        selectedOptions = state.userTags,
                        onOptionClick = { tag -> events.onUserTagChanged(tag as UserTag) },
                        optionLabel = { tag -> getUserTagLabel(tag as UserTag) },
                        isMultiSelect = true,
                        maxSelection = 3,
                        leadingIcon = painterResource(R.drawable.label_icon),
                        isError = state.userTagsIsError,
                        errorText = state.userTagsError?.let { stringResource(it) } ?: ""
                    )
                    TextField(
                        text = state.userDescription,
                        onChange = events.onUserDescriptionChange,
                        label = stringResource(R.string.description),
                        singleLine = false,
                        isError = state.userDescriptionIsError,
                        errorText = state.userDescriptionError?.let { stringResource(it) } ?: "",
                        maxLength = 150
                    )
                    TextField(
                        text = state.accommodationDescription,
                        onChange = events.onAccommodationDescriptionChange,
                        label = stringResource(R.string.accommodationdescription),
                        singleLine = false,
                        isError = state.accommodationDescriptionIsError,
                        errorText = state.accommodationDescriptionError?.let { stringResource(it) }
                            ?: "",
                        maxLength = 1000
                    )
                    HorizontalDivider(
                        thickness = dimensions.tiny,
                        modifier = Modifier.padding(
                            top = dimensions.medium,
                            bottom = dimensions.standard
                        )
                    )
                    val displayPhotos =
                        state.accommodationPicsUrls.filter { it !in state.pendingDeletions } + state.pendingUploads
                    HorizontalPhotoSelector(
                        items = displayPhotos,
                        onAddPhotoClick = onAddPhotoClick,
                        onDeletePhotoClick = { item ->
                            itemToDelete = item
                            activeDialog = DialogConfig(
                                title = R.string.delete_image_desc,
                                body = R.string.delete_image_question,
                                primaryButtonText = R.string.delete,
                                secondaryButtonText = R.string.cancel,
                                icon = R.drawable.logout_icon,
                                mode = true,
                                onConfirm = {
                                    itemToDelete?.let { events.onDeleteAccommodationPhoto(it) }
                                    activeDialog = null
                                },
                                onDismiss = { activeDialog = null }
                            )
                        },
                        modifier = Modifier.padding(bottom = dimensions.standard)
                    )
                    if (state.accommodationPicsIsError) {
                        Text(
                            text = state.accommodationPicsError?.let { stringResource(it) } ?: "",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(
                                bottom = dimensions.small,
                                start = dimensions.large
                            )
                        )
                    }
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
                        value = state.bedrooms,
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
                        keyboardType = KeyboardType.Number,
                        isError = state.squareMetersIsError,
                        errorText = state.squareMetersError?.let { stringResource(it) } ?: ""
                    )
                    HorizontalDivider(
                        thickness = dimensions.tiny,
                        modifier = Modifier.padding(
                            top = dimensions.medium,
                            bottom = dimensions.standard
                        )
                    )
                    if (state.accommodationTagsIsError) {
                        Text(
                            text = state.accommodationTagsError?.let { stringResource(it) } ?: "",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(
                                bottom = dimensions.small,
                                start = dimensions.small
                            )
                        )
                    }
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
            text = stringResource(
                if (isRegisterMode) R.string.register else R.string.save
            ),
            loadingText = stringResource(R.string.saving),
            onClick = onSave,
            isLoading = state.isSaving,
            modifier = Modifier
                .padding(top = dimensions.standard)
                .fillMaxWidth(0.9f),
            textPadding = dimensions.small,
        )

        activeDialog?.let { config ->
            CustomAlertDialog(
                title = config.title,
                body = config.body,
                primaryButtonText = config.primaryButtonText,
                secondaryButtonText = config.secondaryButtonText,
                icon = config.icon,
                mode = config.mode,
                onDismiss = config.onDismiss,
                onConfirm = config.onConfirm
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EditProfilePreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val state = EditProfileState.Success(name = "María del carmen Rodriguez Mérida")
            val events = EditProfileEvents(
                {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}
            )
            EditProfileContent(
                state = state,
                events = events,
                onSave = {},
                onAddPhotoClick = {},
                onProfilePhotoClick = {},
                isRegisterMode = false
            )
        }
    }
}