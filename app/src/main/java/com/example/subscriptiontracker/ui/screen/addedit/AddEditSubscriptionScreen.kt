package com.example.subscriptiontracker.ui.screen.addedit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.subscriptiontracker.domain.model.BillingCycle
import com.example.subscriptiontracker.domain.model.Category
import com.example.subscriptiontracker.util.formatDate
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Add/Edit Subscription screen composable.
 * Provides a form for creating or editing a subscription with fields for name, amount,
 * billing cycle, category, dates, notes, and active status.
 * The save button is rendered as a sticky bottom bar to ensure it is always visible.
 * Date picker fields use [Modifier.clickable] with `enabled = false` on the text field
 * to ensure reliable tap detection.
 *
 * @param onNavigateBack Callback to navigate back after saving or pressing back.
 * @param viewModel The [AddEditSubscriptionViewModel] providing form state and actions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSubscriptionScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddEditSubscriptionViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isEditing) "Edit Subscription" else "Add Subscription") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = viewModel::save,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(if (state.isEditing) "Update Subscription" else "Save Subscription")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Name
            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Subscription Name") },
                isError = state.errors.containsKey("name"),
                supportingText = state.errors["name"]?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Amount
            OutlinedTextField(
                value = state.amount,
                onValueChange = viewModel::onAmountChange,
                label = { Text("Amount") },
                isError = state.errors.containsKey("amount"),
                supportingText = state.errors["amount"]?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Billing Cycle dropdown
            DropdownSelector(
                label = "Billing Cycle",
                selected = state.billingCycle.label,
                options = BillingCycle.entries.map { it.label },
                onSelect = { label ->
                    BillingCycle.entries.find { it.label == label }
                        ?.let(viewModel::onBillingCycleChange)
                }
            )

            // Category dropdown
            DropdownSelector(
                label = "Category",
                selected = state.category.label,
                options = Category.entries.map { it.label },
                onSelect = { label ->
                    Category.entries.find { it.label == label }
                        ?.let(viewModel::onCategoryChange)
                }
            )

            // Next Billing Date
            DateField(
                label = "Next Billing Date",
                date = state.nextBillingDate,
                isError = state.errors.containsKey("nextBillingDate"),
                errorMessage = state.errors["nextBillingDate"],
                onDateSelected = viewModel::onNextBillingDateChange
            )

            // Trial End Date (optional)
            DateField(
                label = "Trial End Date (optional)",
                date = state.trialEndDate,
                onDateSelected = { viewModel.onTrialEndDateChange(it) }
            )

            // Notes
            OutlinedTextField(
                value = state.notes,
                onValueChange = viewModel::onNotesChange,
                label = { Text("Notes (optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 4
            )

            // Active toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Active", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = state.isActive,
                    onCheckedChange = viewModel::onActiveChange
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

/**
 * A date field that shows the selected date and opens a [DatePickerDialog] on tap.
 * Uses [Modifier.clickable] on a [Box] wrapper with the text field `enabled = false`
 * to ensure reliable tap detection (avoids the fragile interactionSource approach).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateField(
    label: String,
    date: LocalDate?,
    isError: Boolean = false,
    errorMessage: String? = null,
    onDateSelected: (LocalDate) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.clickable { showDialog = true }) {
        OutlinedTextField(
            value = date?.let { formatDate(it) } ?: "",
            onValueChange = {},
            label = { Text(label) },
            enabled = false,
            isError = isError,
            supportingText = errorMessage?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }

    if (showDialog) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = date?.atStartOfDay(ZoneId.systemDefault())
                ?.toInstant()?.toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { millis ->
                        val selected = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onDateSelected(selected)
                    }
                    showDialog = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }
}

/**
 * A dropdown selector using [ExposedDropdownMenuBox] for consistent Material 3 styling.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownSelector(
    label: String,
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            singleLine = true
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
