package com.example.praktikum5

import androidx.annotation.DimenRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlinx.coroutines.delay

/* ---------- Helper pembaca resources ---------- */

@Composable
private fun floatRes(@DimenRes id: Int): Float =
    ResourcesCompat.getFloat(LocalContext.current.resources, id)

@Composable
private fun spRes(@DimenRes id: Int): TextUnit = dimensionResource(id).value.sp

/* ---------- Screen utama ---------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormBiodataScreen() {
    val empty = stringResource(R.string.empty_text)

    var firstName by rememberSaveable { mutableStateOf(empty) }
    var lastName by rememberSaveable { mutableStateOf(empty) }
    var email by rememberSaveable { mutableStateOf(empty) }
    var birthDate by rememberSaveable { mutableStateOf(empty) }
    var experience by rememberSaveable { mutableStateOf(empty) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showDropdown by remember { mutableStateOf(false) }
    var showResult by remember { mutableStateOf(false) }

    val experienceOptions = stringArrayResource(R.array.experience_options)
    val skillOptions = stringArrayResource(R.array.skill_options)
    val selectedSkills = remember { mutableStateListOf<String>() }

    val datePattern = stringResource(R.string.date_pattern)
    val timezoneUtc = stringResource(R.string.timezone_utc)
    val dateFormatter = remember(datePattern, timezoneUtc) {
        SimpleDateFormat(datePattern, Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone(timezoneUtc)
        }
    }

    val isFormValid = firstName.isNotBlank() && lastName.isNotBlank() &&
            email.isNotBlank() && birthDate.isNotBlank() &&
            experience.isNotBlank() && selectedSkills.isNotEmpty()

    val weightEqual = floatRes(R.dimen.weight_equal)
    val resultDurationMs = integerResource(R.integer.result_display_duration_ms)

    // Hasil Proceed otomatis hilang setelah durasi di integers.xml (5 detik)
    LaunchedEffect(showResult) {
        if (showResult) {
            delay(resultDurationMs.toLong())
            showResult = false
        }
    }

    Scaffold(containerColor = colorResource(R.color.screen_background)) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {

            // Gambar header di belakang
            Image(
                painter = painterResource(R.drawable.bg_neon_spider),
                contentDescription = stringResource(R.string.cd_header_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(floatRes(R.dimen.header_height_fraction))
            )

            // Box yang MENGAPUNG di atas Scaffold
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(innerPadding)
                    .padding(
                        start = dimensionResource(R.dimen.card_margin_horizontal),
                        end = dimensionResource(R.dimen.card_margin_horizontal),
                        bottom = dimensionResource(R.dimen.card_margin_bottom)
                    )
                    .shadow(
                        elevation = dimensionResource(R.dimen.card_elevation),
                        shape = cardShape(),
                        ambientColor = colorResource(R.color.card_shadow),
                        spotColor = colorResource(R.color.card_shadow)
                    )
                    .fillMaxWidth()
            ) {
                Surface(
                    shape = cardShape(),
                    color = colorResource(R.color.card_background),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(dimensionResource(R.dimen.card_padding)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.form_title),
                            color = colorResource(R.color.title_purple),
                            fontSize = spRes(R.dimen.text_title),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = stringResource(R.string.form_subtitle),
                            color = colorResource(R.color.subtitle_gray),
                            fontSize = spRes(R.dimen.text_subtitle),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(
                                top = dimensionResource(R.dimen.spacing_small),
                                bottom = dimensionResource(R.dimen.spacing_title_bottom)
                            )
                        )

                        // Nama depan + belakang
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(
                                dimensionResource(R.dimen.spacing_field)
                            )
                        ) {
                            BiodataField(
                                value = firstName,
                                onValueChange = { firstName = it },
                                placeholder = stringResource(R.string.hint_first_name),
                                modifier = Modifier.weight(weightEqual)
                            )
                            BiodataField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                placeholder = stringResource(R.string.hint_last_name),
                                modifier = Modifier.weight(weightEqual)
                            )
                        }

                        Spacer(Modifier.height(dimensionResource(R.dimen.spacing_field)))

                        // Email
                        BiodataField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = stringResource(R.string.hint_email),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(dimensionResource(R.dimen.spacing_field)))

                        // Tgl lahir + experience
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(
                                dimensionResource(R.dimen.spacing_field)
                            )
                        ) {
                            BiodataField(
                                value = birthDate,
                                onValueChange = {},
                                placeholder = stringResource(R.string.hint_birth_date),
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { showDatePicker = true }) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_calendar),
                                            contentDescription = stringResource(R.string.cd_pick_date),
                                            tint = colorResource(R.color.field_icon)
                                        )
                                    }
                                },
                                modifier = Modifier.weight(weightEqual)
                            )

                            Box(modifier = Modifier.weight(weightEqual)) {
                                BiodataField(
                                    value = experience,
                                    onValueChange = {},
                                    placeholder = stringResource(R.string.hint_experience),
                                    readOnly = true,
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_arrow_drop_down),
                                            contentDescription = stringResource(R.string.cd_open_dropdown),
                                            tint = colorResource(R.color.field_icon)
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clickable { showDropdown = true }
                                )
                                DropdownMenu(
                                    expanded = showDropdown,
                                    onDismissRequest = { showDropdown = false }
                                ) {
                                    experienceOptions.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(option) },
                                            onClick = {
                                                experience = option
                                                showDropdown = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(dimensionResource(R.dimen.spacing_section)))

                        // Skill
                        Text(
                            text = stringResource(R.string.label_skill),
                            color = colorResource(R.color.skill_label),
                            fontSize = spRes(R.dimen.text_label),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            skillOptions.forEach { skill ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = skill in selectedSkills,
                                        onCheckedChange = { checked ->
                                            if (checked) selectedSkills.add(skill)
                                            else selectedSkills.remove(skill)
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = colorResource(R.color.checkbox_checked),
                                            uncheckedColor = colorResource(R.color.checkbox_unchecked),
                                            checkmarkColor = colorResource(R.color.checkbox_checkmark)
                                        )
                                    )
                                    Text(
                                        text = skill,
                                        color = colorResource(R.color.field_text),
                                        fontSize = spRes(R.dimen.text_skill)
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(dimensionResource(R.dimen.spacing_before_button)))

                        // Tombol Proceed
                        Button(
                            onClick = { showResult = true },
                            enabled = isFormValid,
                            shape = RoundedCornerShape(dimensionResource(R.dimen.corner_button)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.button_enabled_container),
                                contentColor = colorResource(R.color.button_enabled_content),
                                disabledContainerColor = colorResource(R.color.button_disabled_container),
                                disabledContentColor = colorResource(R.color.button_disabled_content)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(dimensionResource(R.dimen.button_height))
                        ) {
                            Text(
                                text = stringResource(R.string.button_proceed),
                                fontSize = spRes(R.dimen.text_button),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    // Hasil Proceed: tampil 5 detik lalu hilang otomatis
    if (showResult) {
        AlertDialog(
            onDismissRequest = { showResult = false },
            containerColor = colorResource(R.color.card_background),
            titleContentColor = colorResource(R.color.title_purple),
            textContentColor = colorResource(R.color.field_text),
            title = { Text(stringResource(R.string.result_title)) },
            text = {
                Text(
                    stringResource(
                        R.string.result_message,
                        firstName,
                        lastName,
                        email,
                        birthDate,
                        experience,
                        selectedSkills.joinToString(stringResource(R.string.skill_separator))
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = { showResult = false }) {
                    Text(
                        text = stringResource(R.string.dialog_close),
                        color = colorResource(R.color.title_purple)
                    )
                }
            }
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        birthDate = dateFormatter.format(Date(it))
                    }
                    showDatePicker = false
                }) { Text(stringResource(R.string.dialog_ok)) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.dialog_cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

/* ---------- Komponen reusable ---------- */

@Composable
private fun cardShape() = RoundedCornerShape(
    topStart = dimensionResource(R.dimen.corner_card_top),
    topEnd = dimensionResource(R.dimen.corner_card_top),
    bottomStart = dimensionResource(R.dimen.corner_card_bottom),
    bottomEnd = dimensionResource(R.dimen.corner_card_bottom)
)

@Composable
private fun BiodataField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        singleLine = true,
        placeholder = {
            Text(
                text = placeholder,
                fontSize = spRes(R.dimen.text_field)
            )
        },
        textStyle = LocalTextStyle.current.copy(fontSize = spRes(R.dimen.text_field)),
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_field)),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(R.color.field_border_focused),
            unfocusedBorderColor = colorResource(R.color.field_border),
            focusedTextColor = colorResource(R.color.field_text),
            unfocusedTextColor = colorResource(R.color.field_text),
            focusedPlaceholderColor = colorResource(R.color.field_placeholder),
            unfocusedPlaceholderColor = colorResource(R.color.field_placeholder),
            cursorColor = colorResource(R.color.field_border_focused)
        ),
        modifier = modifier
    )
}