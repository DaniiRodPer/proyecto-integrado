package com.dam.proydrp.ui.components.pickers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.dam.proydrp.R
import com.dam.proydrp.ui.common.LocalDimensions
import com.dam.proydrp.ui.components.text.TextField
import com.dam.proydrp.ui.theme.ProydrpTheme
import com.dam.proydrp.ui.utils.localDateToMilis
import com.dam.proydrp.ui.utils.localDateToString
import com.dam.proydrp.ui.utils.milisToLocalDate
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate?) -> Unit,
    modifier: Modifier = Modifier,
    intervalRestric: Boolean = true,
    icon: Painter? = null,
    defaultShowModal: Boolean = true,
    isError: Boolean = false,
    errorText: String = ""
) {
    val dimensions = LocalDimensions.current
    var showModal by remember { mutableStateOf(defaultShowModal) }

    val minDate = remember {
        localDateToMilis(LocalDate.of(1900, 1, 1))!!
    }
    val maxDate = remember {
        localDateToMilis(LocalDate.now().minusYears(18))!!
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = localDateToMilis(selectedDate),
        initialDisplayedMonthMillis = localDateToMilis(selectedDate) ?: maxDate,
        selectableDates = if (intervalRestric) {
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis in minDate..maxDate
                }

                override fun isSelectableYear(year: Int): Boolean {
                    return year in 1900..LocalDate.now().year
                }
            }
        } else {
            DatePickerDefaults.AllDates
        }
    )

    val colors = DatePickerDefaults.colors(
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.primary,
        headlineContentColor = MaterialTheme.colorScheme.secondary,
        weekdayContentColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
        subheadContentColor = MaterialTheme.colorScheme.secondary,
        navigationContentColor = MaterialTheme.colorScheme.onBackground,
        selectedDayContainerColor = MaterialTheme.colorScheme.tertiary,
        selectedDayContentColor = MaterialTheme.colorScheme.primary,
        todayContentColor = MaterialTheme.colorScheme.tertiary,
        todayDateBorderColor = MaterialTheme.colorScheme.tertiary,
        dayContentColor = MaterialTheme.colorScheme.onBackground,
        disabledDayContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.38f),
        yearContentColor = MaterialTheme.colorScheme.onBackground,
        selectedYearContentColor = MaterialTheme.colorScheme.primary,
        selectedYearContainerColor = MaterialTheme.colorScheme.tertiary,
        dividerColor = MaterialTheme.colorScheme.outlineVariant
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                text = selectedDate?.let { localDateToString(it) } ?: "",
                onChange = {},
                icon = icon,
                readOnly = true,
                isError = isError,
                errorText = errorText,
                label = stringResource(R.string.select_date_placeholder)
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showModal = true }
            )
        }



        if (showModal) {
            DatePickerDialog(
                onDismissRequest = { showModal = false },
                confirmButton = {
                    TextButton(onClick = {
                        onDateSelected(milisToLocalDate(datePickerState.selectedDateMillis))
                        showModal = false
                    }) {
                        Text(stringResource(R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showModal = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                },
                colors = colors
            ) {
                DatePicker(
                    title = {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(
                                stringResource(R.string.select_date),
                                textAlign = TextAlign.Center,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = dimensions.big)
                            )
                        }
                    },
                    state = datePickerState,
                    colors = colors
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DatePickerFieldPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            DatePickerField(
                label = "Select Date",
                selectedDate = LocalDate.now(),
                onDateSelected = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DatePickerPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            DatePickerField(
                label = "Select Date",
                selectedDate = LocalDate.now(),
                onDateSelected = {},
                defaultShowModal = true
            )
        }
    }
}