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
