package com.zanoapps.auth.presentation.components


import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zanoapps.core.presentation.designsystem.ActionButtonGradient
import com.zanoapps.core.presentation.designsystem.BackIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.BalkanEstateWhite95
import com.zanoapps.core.presentation.designsystem.BalkanEstateWhite95Background
import com.zanoapps.core.presentation.designsystem.CheckIcon
import com.zanoapps.core.presentation.designsystem.CompareIcon
import com.zanoapps.core.presentation.designsystem.FiltersIcon
import com.zanoapps.core.presentation.designsystem.HomeIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.PrimaryGradient
import com.zanoapps.core.presentation.designsystem.SortResultsIcon
import com.zanoapps.core.presentation.designsystem.TourIcon
import com.zanoapps.core.presentation.designsystem.VirtualTourIcon
import java.text.NumberFormat
import java.util.Currency

@Composable
private fun SavesHeader(
    propertyCount: Int,
    rentalCount: Int,
    onSortClick: () -> Unit,
    onFilterClick: () -> Unit,
    onCompareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text(
            text = "Your saves",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "${propertyCount - rentalCount} home${if (propertyCount - rentalCount != 1) "s" else ""} and $rentalCount rental${if (rentalCount != 1) "s" else ""}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            ActionButton(
                icon = BackIcon,
                text = "Sort",
                onClick = onSortClick
            )

            ActionButton(
                icon = BackIcon,
                text = "Filter",
                onClick = onFilterClick
            )

            ActionButton(
                icon = BackIcon,
                text = "Compare",
                onClick = onCompareClick
            )
        }
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            width = 1.dp,
            brush = ActionButtonGradient
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 1.dp,
            pressedElevation = 3.dp
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
private fun NavigationSection(
    onToursClick: () -> Unit,
    onContactedAgentsClick: () -> Unit,
    onClaimHomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            NavigationItem(
                icon = BackIcon,
                text = "Tours",
                onClick = onToursClick
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )

            NavigationItem(
                icon = BackIcon,
                text = "Contacted agents",
                onClick = onContactedAgentsClick
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )

            NavigationItem(
                icon = Icons.Default.Home,
                text = "Claim your home",
                onClick = onClaimHomeClick
            )
        }
    }
}

@Composable
private fun NavigationItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // FIX: Create interaction source and pass indication explicitly
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private fun formatPriceWithPeriod(price: Double, currencyCode: String, listingType: String): String {
    val formatter = NumberFormat.getCurrencyInstance()
    return try {
        formatter.currency = Currency.getInstance(currencyCode)
        formatter.maximumFractionDigits = if (listingType == "Rent") 2 else 0
        val formattedPrice = formatter.format(price)
        if (listingType == "Rent") "$formattedPrice / month" else formattedPrice
    } catch (e: Exception) {
        val basicFormat = NumberFormat.getInstance().format(price)
        if (listingType == "Rent") "€$basicFormat / month" else "€$basicFormat"
    }
}

// Extended Property data class with additional fields
data class Property(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val currency: String,
    val address: String,
    val bedrooms: Int,
    val bathrooms: Int,
    val squareFootage: Int,
    val imageUrl: String,
    val images: List<String> = listOf(),
    val latitude: Double,
    val longitude: Double,
    val propertyType: String,
    val listingType: String, // "Sale" or "Rent"
    val agentId: String,
    val agentName: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val isFavorite: Boolean = false,
    val additionalFeatures: String? = null
)

private fun getSampleProperties(): List<Property> = listOf(
    Property(
        id = "1",
        title = "Beautiful Family Home",
        description = "A stunning 3-bedroom house with modern amenities",
        price = 800.00,
        currency = "EUR",
        address = "Rruga Myslym Shyri 27, Tirana",
        bedrooms = 3,
        bathrooms = 2,
        squareFootage = 1800,
        imageUrl = "https://example.com/property1.jpg",
        images = listOf(
            "https://example.com/property1.jpg",
            "https://example.com/property1-2.jpg",
            "https://example.com/property1-3.jpg"
        ),
        latitude = 41.3275,
        longitude = 19.8189,
        propertyType = "House",
        listingType = "Rent",
        agentId = "agent1",
        agentName = "Besmir Kola",
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        isFavorite = true,
        additionalFeatures = "4 beds · 3 baths · 2,000 sqft"
    ),
    Property(
        id = "2",
        title = "Luxury Villa",
        description = "Exclusive waterfront property with stunning views",
        price = 4175000.00,
        currency = "EUR",
        address = "Rruga Myslym Shyri 27, Tirana",
        bedrooms = 3,
        bathrooms = 2,
        squareFootage = 1800,
        imageUrl = "https://example.com/property2.jpg",
        images = listOf(
            "https://example.com/property2.jpg",
            "https://example.com/property2-2.jpg",
            "https://example.com/property2-3.jpg"
        ),
        latitude = 41.3275,
        longitude = 19.8189,
        propertyType = "Villa",
        listingType = "Sale",
        agentId = "agent1",
        agentName = "Besmir Kola",
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        isFavorite = true,
        additionalFeatures = "4 beds · 3 baths · 2,000 sqft"
    )
)

@Preview(showBackground = true)
@Composable
private fun SavesScreenPreview() {
    BalkanEstateTheme {
        Column {
            NavigationItem(
                icon = TourIcon,
                text = "Tours",
                onClick = { },
            )
            Spacer(modifier = Modifier.width(16.dp))

            NavigationItem(
                icon = HomeIcon,
                text = "Contact Agent",
                onClick = { }
            )
            Spacer(modifier = Modifier.width(16.dp))

            NavigationItem(
                icon = VirtualTourIcon,
                text = "Save Property",
                onClick = { }
            )
            Spacer(modifier = Modifier.width(16.dp))




        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NavigationSectionPrev() {

    NavigationSection(
        onToursClick = { },
        onContactedAgentsClick = {  },
        onClaimHomeClick = { },

    )
    
}


@Preview(showBackground = true)
@Composable
private fun ActionButtonPreview() {

    Row (
        modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max).padding(16.dp),
        horizontalArrangement =  Arrangement.SpaceEvenly
    ) {

        ActionButton(
            icon = FiltersIcon,
            text = "Filter",
            onClick = {  },
        )

        Spacer(modifier = Modifier.width(16.dp))


        ActionButton(
            icon = SortResultsIcon,
            text = "Sort",
            onClick = {  },
        )

        Spacer(modifier = Modifier.width(16.dp))

        ActionButton(
            icon = CompareIcon,
            text = "Compare",
            onClick = {  },
        )



    }


}
