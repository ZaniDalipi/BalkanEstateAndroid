package com.zanoapps.search.presentation.filter.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.domain.enums.ListingType
import com.zanoapps.core.domain.enums.PropertyType
import com.zanoapps.core.domain.enums.SortOption
import com.zanoapps.core.presentation.designsystem.BalkanEstateGradientStartPrimary
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.CrossIcon
import com.zanoapps.core.presentation.designsystem.FiltersIcon
import com.zanoapps.core.presentation.designsystem.Poppins
import com.zanoapps.core.presentation.designsystem.SortResultsIcon
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateActiveFilterChip
import com.zanoapps.search.presentation.filter.FilterChipData

/**
 * A horizontal bar showing currently active filters with the ability to remove them.
 * Appears below the search bar when filters are active.
 */
@Composable
fun ActiveFiltersBar(
    activeFilters: List<FilterChipData>,
    sortOption: SortOption,
    onRemoveFilter: (FilterChipData) -> Unit,
    onClearAll: () -> Unit,
    onOpenFilters: () -> Unit,
    onOpenSort: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = activeFilters.isNotEmpty(),
        enter = expandVertically(animationSpec = tween(200)) + fadeIn(animationSpec = tween(200)),
        exit = shrinkVertically(animationSpec = tween(200)) + fadeOut(animationSpec = tween(200))
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${activeFilters.size} ${if (activeFilters.size == 1) "filter" else "filters"} applied",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Poppins,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Text(
                    text = "Clear all",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Poppins,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(onClick = onClearAll)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Sort chip
                SortChip(
                    sortOption = sortOption,
                    onClick = onOpenSort
                )

                // Filter chips
                activeFilters.forEach { filter ->
                    BalkanEstateActiveFilterChip(
                        text = filter.displayText,
                        onRemove = { onRemoveFilter(filter) }
                    )
                }
            }

            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}

/**
 * Compact bar showing filter and sort buttons with count badges
 */
@Composable
fun FilterSortBar(
    activeFilterCount: Int,
    sortOption: SortOption,
    onOpenFilters: () -> Unit,
    onOpenSort: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Filter button
        FilterButton(
            count = activeFilterCount,
            onClick = onOpenFilters,
            modifier = Modifier.weight(1f)
        )

        // Sort button
        SortButton(
            sortOption = sortOption,
            onClick = onOpenSort,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Filter button with count badge
 */
@Composable
private fun FilterButton(
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = FiltersIcon,
            contentDescription = "Filters",
            modifier = Modifier.size(18.dp),
            tint = if (count > 0) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Filters",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Poppins,
            color = if (count > 0) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
        if (count > 0) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = count.toString(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

/**
 * Sort button showing current sort option
 */
@Composable
private fun SortButton(
    sortOption: SortOption,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = SortResultsIcon,
            contentDescription = "Sort",
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = sortOption.displayName,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Poppins,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )
    }
}

/**
 * Compact sort chip for the active filters row
 */
@Composable
private fun SortChip(
    sortOption: SortOption,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = SortResultsIcon,
            contentDescription = "Sort",
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = sortOption.displayName,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Poppins,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

/**
 * Results count and applied filters summary
 */
@Composable
fun SearchResultsSummary(
    resultsCount: Int,
    activeFilterCount: Int,
    sortOption: SortOption,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$resultsCount ${if (resultsCount == 1) "property" else "properties"} found",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Poppins,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (activeFilterCount > 0) {
                Text(
                    text = "$activeFilterCount ${if (activeFilterCount == 1) "filter" else "filters"}",
                    fontSize = 12.sp,
                    fontFamily = Poppins,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = " · ",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
            Text(
                text = sortOption.displayName,
                fontSize = 12.sp,
                fontFamily = Poppins,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActiveFiltersBarPreview() {
    BalkanEstateTheme {
        ActiveFiltersBar(
            activeFilters = listOf(
                FilterChipData.ListingTypeChip(ListingType.SALE),
                FilterChipData.PropertyTypeChip(PropertyType.APARTMENT),
                FilterChipData.PriceRangeChip("100000", "500000"),
                FilterChipData.BedroomsChip(2, null)
            ),
            sortOption = SortOption.PRICE_LOW_TO_HIGH,
            onRemoveFilter = {},
            onClearAll = {},
            onOpenFilters = {},
            onOpenSort = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterSortBarPreview() {
    BalkanEstateTheme {
        FilterSortBar(
            activeFilterCount = 3,
            sortOption = SortOption.NEWEST,
            onOpenFilters = {},
            onOpenSort = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchResultsSummaryPreview() {
    BalkanEstateTheme {
        SearchResultsSummary(
            resultsCount = 45,
            activeFilterCount = 2,
            sortOption = SortOption.PRICE_LOW_TO_HIGH
        )
    }
}
