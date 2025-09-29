package com.zanoapps.onboarding.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.Poppins

enum class SelectionType {
    RADIO,
    CHECKBOX
}

@Composable
fun BalkanEstateSelectionCard(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    selectionType: SelectionType = SelectionType.CHECKBOX,
    showSelectionIndicator: Boolean = true
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primary.copy(0.05f)
            } else {
                MaterialTheme.colorScheme.secondary
            }
        ),
        border = if (isSelected) {
            BorderStroke(
                2.dp,
                MaterialTheme.colorScheme.primary
            )
        } else {
            BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 2.dp else 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showSelectionIndicator) {
                when (selectionType) {
                    SelectionType.RADIO -> {
                        RadioButton(
                            selected = isSelected,
                            onClick = onClick,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = MaterialTheme.colorScheme.surface
                            ),
                        )
                    }
                    SelectionType.CHECKBOX -> {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { onClick() },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.primary,
                                uncheckedColor = MaterialTheme.colorScheme.surface,
                                checkmarkColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }

            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = Poppins,
                    )

                Text(
                    text = description,
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}

// Helper composable for multiple checkbox selection
@Composable
fun MultiSelectCheckboxGroup(
    options: List<SelectionOption>,
    selectedOptions: Set<String>,
    onSelectionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    showIcons: Boolean = false
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        options.forEach { option ->
            BalkanEstateSelectionCard(
                title = option.title,
                description = option.description,
                isSelected = selectedOptions.contains(option.id),
                onClick = { onSelectionChange(option.id) },
                icon = if (showIcons) option.icon else null,
                selectionType = SelectionType.CHECKBOX
            )
        }
    }
}

// this has to be created separately
// in in a data class wherever will be used, so in on boarding module
data class SelectionOption(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector? = null
)

@Preview(showBackground = true)
@Composable
private fun SelectionCardCheckboxPreview() {
    BalkanEstateTheme {
        var selectedOptions by remember { mutableStateOf(setOf<String>()) }

        val options = listOf(
            SelectionOption(
                id = "young_professional",
                title = "Young Professional",
                description = "Starting career, looking for convenient location"
            ),
            SelectionOption(
                id = "growing_family",
                title = "Growing Family",
                description = "Need space and family-friendly amenities"
            ),
            SelectionOption(
                id = "established_family",
                title = "Established Family",
                description = "Looking for quality schools and community"
            ),
            SelectionOption(
                id = "empty_nesters",
                title = "Empty Nesters",
                description = "Ready to downsize and enjoy convenience"
            )
        )

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Multiple Selection Example",
                style = MaterialTheme.typography.headlineSmall,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Selected: ${selectedOptions.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = Poppins,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            MultiSelectCheckboxGroup(
                options = options,
                selectedOptions = selectedOptions,
                onSelectionChange = { optionId ->
                    selectedOptions = if (selectedOptions.contains(optionId)) {
                        selectedOptions - optionId
                    } else {
                        selectedOptions + optionId
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SingleSelectionRadioPreview() {
    BalkanEstateTheme {
        var selectedOption by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Single Selection Example (Radio)",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = Poppins,
            )

            Text(
                text = "Selected: $selectedOption",
                fontFamily = Poppins,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            BalkanEstateSelectionCard(
                title = "Buy",
                description = "I want to purchase a property",
                isSelected = selectedOption == "buy",
                onClick = { selectedOption = "buy" },
                selectionType = SelectionType.RADIO
            )

            BalkanEstateSelectionCard(
                title = "Rent",
                description = "I'm looking for a rental property",
                isSelected = selectedOption == "rent",
                onClick = { selectedOption = "rent" },
                selectionType = SelectionType.RADIO
            )
        }
    }
}