package com.zanoapps.search.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.SparkleIcon

data class SearchFiltersData(
    val country: String = "All Countries",
    val searchLocation: String = "",
    val minPrice: String = "",
    val maxPrice: String = "",
    val minArea: String = "",
    val maxArea: String = "",
    val propertyTypes: Set<String> = emptySet(),
    val bedrooms: Int? = null,
    val bathrooms: Int? = null,
    val amenities: Set<String> = emptySet(),
    val listingType: String? = null
)

data class AIChatMessage(
    val id: String,
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFiltersScreen(
    isVisible: Boolean,
    filters: SearchFiltersData,
    aiMessages: List<AIChatMessage>,
    isAITyping: Boolean,
    onDismiss: () -> Unit,
    onFiltersChange: (SearchFiltersData) -> Unit,
    onResetFilters: () -> Unit,
    onSaveSearch: () -> Unit,
    onShowResults: () -> Unit,
    onAIMessageSend: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isVisible) return

    var isAISearchMode by remember { mutableStateOf(false) }
    var localFilters by remember(filters) { mutableStateOf(filters) }
    var aiInputText by remember { mutableStateOf("") }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            // Header
            FilterHeader(onDismiss = onDismiss)

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

            // Country Dropdown
            CountryDropdown(
                selectedCountry = localFilters.country,
                onCountrySelected = { country ->
                    localFilters = localFilters.copy(country = country)
                    onFiltersChange(localFilters)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Search Mode Toggle
            SearchModeToggle(
                isAISearchMode = isAISearchMode,
                onModeChange = { isAISearchMode = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Content based on mode
            if (isAISearchMode) {
                // AI Search Mode
                AISearchContent(
                    messages = aiMessages,
                    isTyping = isAITyping,
                    inputText = aiInputText,
                    onInputChange = { aiInputText = it },
                    onSendMessage = {
                        if (aiInputText.isNotBlank()) {
                            onAIMessageSend(aiInputText)
                            aiInputText = ""
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            } else {
                // Traditional Search Mode
                TraditionalSearchContent(
                    filters = localFilters,
                    onFiltersChange = { newFilters ->
                        localFilters = newFilters
                        onFiltersChange(newFilters)
                    },
                    modifier = Modifier.weight(1f)
                )

                // Bottom Action Buttons
                FilterActionButtons(
                    onReset = {
                        localFilters = SearchFiltersData()
                        onResetFilters()
                    },
                    onSaveSearch = onSaveSearch,
                    onShowResults = onShowResults
                )
            }
        }
    }
}

@Composable
private fun FilterHeader(
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Filters",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountryDropdown(
    selectedCountry: String,
    onCountrySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val countries = listOf(
        "All Countries",
        "Albania",
        "Kosovo",
        "North Macedonia",
        "Montenegro",
        "Serbia",
        "Bosnia and Herzegovina",
        "Croatia",
        "Slovenia",
        "Greece",
        "Bulgaria"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Country",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedCountry,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = BalkanEstatePrimaryBlue
                ),
                shape = RoundedCornerShape(8.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                countries.forEach { country ->
                    DropdownMenuItem(
                        text = { Text(country) },
                        onClick = {
                            onCountrySelected(country)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchModeToggle(
    isAISearchMode: Boolean,
    onModeChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFF5F5F5))
            .padding(4.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        // Search Properties Tab
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(if (!isAISearchMode) Color.White else Color.Transparent)
                .clickable { onModeChange(false) }
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Search Properties",
                fontSize = 14.sp,
                fontWeight = if (!isAISearchMode) FontWeight.SemiBold else FontWeight.Normal,
                color = if (!isAISearchMode) BalkanEstatePrimaryBlue else Color.Gray
            )
        }

        // AI Search Tab
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(if (isAISearchMode) Color.White else Color.Transparent)
                .clickable { onModeChange(true) }
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = SparkleIcon,
                    contentDescription = null,
                    tint = if (isAISearchMode) BalkanEstatePrimaryBlue else Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "AI Search",
                    fontSize = 14.sp,
                    fontWeight = if (isAISearchMode) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isAISearchMode) BalkanEstatePrimaryBlue else Color.Gray
                )
            }
        }
    }
}

@Composable
private fun TraditionalSearchContent(
    filters: SearchFiltersData,
    onFiltersChange: (SearchFiltersData) -> Unit,
    modifier: Modifier = Modifier
) {
    var isAdvancedExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        // Search Location
        FilterInputField(
            label = "Search Location",
            value = filters.searchLocation,
            onValueChange = { onFiltersChange(filters.copy(searchLocation = it)) },
            placeholder = "Search city, address...",
            leadingIcon = {
                Icon(
                    imageVector = LocationIcon,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Price Range
        Text(
            text = "Price Range",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PriceInputField(
                value = filters.minPrice,
                onValueChange = { onFiltersChange(filters.copy(minPrice = it)) },
                placeholder = "Min",
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "-",
                modifier = Modifier.align(Alignment.CenterVertically),
                color = Color.Gray
            )
            PriceInputField(
                value = filters.maxPrice,
                onValueChange = { onFiltersChange(filters.copy(maxPrice = it)) },
                placeholder = "Max",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Area
        Text(
            text = "Area (m²)",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AreaInputField(
                value = filters.minArea,
                onValueChange = { onFiltersChange(filters.copy(minArea = it)) },
                placeholder = "Min",
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "-",
                modifier = Modifier.align(Alignment.CenterVertically),
                color = Color.Gray
            )
            AreaInputField(
                value = filters.maxArea,
                onValueChange = { onFiltersChange(filters.copy(maxArea = it)) },
                placeholder = "Max",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Advanced Filters Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isAdvancedExpanded = !isAdvancedExpanded }
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Advanced Filters",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Icon(
                imageVector = if (isAdvancedExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (isAdvancedExpanded) "Collapse" else "Expand",
                tint = Color.Gray
            )
        }

        // Advanced Filters Content
        AnimatedVisibility(
            visible = isAdvancedExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            AdvancedFiltersContent(
                filters = filters,
                onFiltersChange = onFiltersChange
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AdvancedFiltersContent(
    filters: SearchFiltersData,
    onFiltersChange: (SearchFiltersData) -> Unit
) {
    Column {
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

        Spacer(modifier = Modifier.height(16.dp))

        // Listing Type
        Text(
            text = "Listing Type",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilterChipCompact(
                text = "For Sale",
                isSelected = filters.listingType == "Sale",
                onClick = {
                    onFiltersChange(filters.copy(
                        listingType = if (filters.listingType == "Sale") null else "Sale"
                    ))
                }
            )
            FilterChipCompact(
                text = "For Rent",
                isSelected = filters.listingType == "Rent",
                onClick = {
                    onFiltersChange(filters.copy(
                        listingType = if (filters.listingType == "Rent") null else "Rent"
                    ))
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Property Type
        Text(
            text = "Property Type",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        val propertyTypes = listOf("Apartment", "House", "Villa", "Land", "Commercial", "Office")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            propertyTypes.forEach { type ->
                FilterChipCompact(
                    text = type,
                    isSelected = filters.propertyTypes.contains(type),
                    onClick = {
                        val newTypes = if (filters.propertyTypes.contains(type)) {
                            filters.propertyTypes - type
                        } else {
                            filters.propertyTypes + type
                        }
                        onFiltersChange(filters.copy(propertyTypes = newTypes))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bedrooms
        Text(
            text = "Bedrooms",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Any" to null, "1+" to 1, "2+" to 2, "3+" to 3, "4+" to 4).forEach { (label, value) ->
                FilterChipCompact(
                    text = label,
                    isSelected = filters.bedrooms == value,
                    onClick = { onFiltersChange(filters.copy(bedrooms = value)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bathrooms
        Text(
            text = "Bathrooms",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Any" to null, "1+" to 1, "2+" to 2, "3+" to 3).forEach { (label, value) ->
                FilterChipCompact(
                    text = label,
                    isSelected = filters.bathrooms == value,
                    onClick = { onFiltersChange(filters.copy(bathrooms = value)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun FilterInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.LightGray) },
            leadingIcon = leadingIcon,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = BalkanEstatePrimaryBlue
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )
    }
}

@Composable
private fun PriceInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.all { char -> char.isDigit() }) onValueChange(it) },
        placeholder = { Text(placeholder, color = Color.LightGray) },
        leadingIcon = {
            Text("€", color = Color.Gray, fontSize = 16.sp)
        },
        modifier = modifier,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.LightGray,
            focusedBorderColor = BalkanEstatePrimaryBlue
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
private fun AreaInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.all { char -> char.isDigit() }) onValueChange(it) },
        placeholder = { Text(placeholder, color = Color.LightGray) },
        trailingIcon = {
            Text("m²", color = Color.Gray, fontSize = 14.sp)
        },
        modifier = modifier,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.LightGray,
            focusedBorderColor = BalkanEstatePrimaryBlue
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
private fun FilterChipCompact(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) BalkanEstatePrimaryBlue else Color.White)
            .border(
                width = 1.dp,
                color = if (isSelected) BalkanEstatePrimaryBlue else Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.DarkGray,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun FilterActionButtons(
    onReset: () -> Unit,
    onSaveSearch: () -> Unit,
    onShowResults: () -> Unit
) {
    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onReset,
            modifier = Modifier.weight(0.8f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.DarkGray
            )
        ) {
            Text("Reset")
        }

        OutlinedButton(
            onClick = onSaveSearch,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.DarkGray
            )
        ) {
            Text("Save Search")
        }

        Button(
            onClick = onShowResults,
            modifier = Modifier.weight(1.2f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BalkanEstatePrimaryBlue
            )
        ) {
            Text("Show Results", color = Color.White)
        }
    }
}

@Composable
private fun AISearchContent(
    messages: List<AIChatMessage>,
    isTyping: Boolean,
    inputText: String,
    onInputChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Chat Messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            reverseLayout = false
        ) {
            items(messages) { message ->
                ChatMessageBubble(message = message)
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (isTyping) {
                item {
                    TypingIndicator()
                }
            }
        }

        // Input Field
        ChatInputField(
            value = inputText,
            onValueChange = onInputChange,
            onSend = onSendMessage,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
private fun ChatMessageBubble(
    message: AIChatMessage
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isFromUser) {
            // AI Avatar
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = SparkleIcon,
                    contentDescription = null,
                    tint = BalkanEstatePrimaryBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (message.isFromUser) BalkanEstatePrimaryBlue
                    else Color(0xFFF5F5F5)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = message.content,
                color = if (message.isFromUser) Color.White else Color.Black,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun TypingIndicator() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = SparkleIcon,
                contentDescription = null,
                tint = BalkanEstatePrimaryBlue,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF5F5F5))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 14.sp,
                color = Color.Black
            ),
            cursorBrush = SolidColor(BalkanEstatePrimaryBlue),
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { onSend() }),
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = "Tell me what you're looking for...",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                    innerTextField()
                }
            }
        )

        IconButton(
            onClick = onSend,
            enabled = value.isNotBlank()
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = if (value.isNotBlank()) BalkanEstatePrimaryBlue else Color.LightGray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchFiltersScreenPreview() {
    BalkanEstateTheme {
        SearchFiltersScreen(
            isVisible = true,
            filters = SearchFiltersData(),
            aiMessages = listOf(
                AIChatMessage(
                    id = "1",
                    content = "Hello! Welcome to Balkan Estate. How can I help you find a property today?",
                    isFromUser = false
                ),
                AIChatMessage(
                    id = "2",
                    content = "i need a house in tirana",
                    isFromUser = true
                )
            ),
            isAITyping = true,
            onDismiss = {},
            onFiltersChange = {},
            onResetFilters = {},
            onSaveSearch = {},
            onShowResults = {},
            onAIMessageSend = {}
        )
    }
}
