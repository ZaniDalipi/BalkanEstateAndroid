package com.zanoapps.agent.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.BuildingIcon
import com.zanoapps.core.presentation.designsystem.CalendarIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.PhoneIcon
import com.zanoapps.core.presentation.designsystem.Poppins
import com.zanoapps.core.presentation.designsystem.RatingStarIcon
import com.zanoapps.core.presentation.designsystem.VerifiedIcon

data class Agency(
    val id: String,
    val name: String,
    val description: String,
    val location: String,
    val rating: Float,
    val reviewsCount: Int,
    val agentsCount: Int,
    val activeListings: Int,
    val yearEstablished: Int,
    val isVerified: Boolean = false,
    val specializations: List<String> = emptyList(),
    val logoInitial: String = name.take(1).uppercase()
)

@Composable
fun AgenciesScreenRoot(
    agencies: List<Agency>,
    onAgencyClick: (Agency) -> Unit,
    onViewListingsClick: (Agency) -> Unit,
    modifier: Modifier = Modifier
) {
    AgenciesScreen(
        agencies = agencies,
        onAgencyClick = onAgencyClick,
        onViewListingsClick = onViewListingsClick,
        modifier = modifier
    )
}

@Composable
fun AgenciesScreen(
    agencies: List<Agency>,
    onAgencyClick: (Agency) -> Unit,
    onViewListingsClick: (Agency) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = "Real Estate Agencies",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        fontFamily = Poppins
                    )
                    Text(
                        text = "Browse trusted agencies across the Balkans",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // Agencies List
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = agencies,
                    key = { it.id }
                ) { agency ->
                    AgencyCard(
                        agency = agency,
                        onClick = { onAgencyClick(agency) },
                        onViewListingsClick = { onViewListingsClick(agency) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AgencyCard(
    agency: Agency,
    onClick: () -> Unit,
    onViewListingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Logo
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = BuildingIcon,
                        contentDescription = null,
                        tint = BalkanEstatePrimaryBlue,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // Name with verified badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = agency.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        if (agency.isVerified) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = VerifiedIcon,
                                contentDescription = "Verified",
                                tint = BalkanEstateGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = RatingStarIcon,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", agency.rating),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray
                        )
                        Text(
                            text = " (${agency.reviewsCount} reviews)",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Location
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = LocationIcon,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = agency.location,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = agency.description,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AgencyStatItem(
                    icon = PersonIcon,
                    value = agency.agentsCount.toString(),
                    label = "Agents"
                )
                AgencyStatItem(
                    icon = BuildingIcon,
                    value = agency.activeListings.toString(),
                    label = "Listings"
                )
                AgencyStatItem(
                    icon = CalendarIcon,
                    value = "Since ${agency.yearEstablished}",
                    label = "Established"
                )
            }

            // Specializations
            if (agency.specializations.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(agency.specializations) { spec ->
                        AgencySpecializationChip(text = spec)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = BalkanEstatePrimaryBlue
                    )
                ) {
                    Text(
                        text = "Learn More",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Button(
                    onClick = onViewListingsClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BalkanEstatePrimaryBlue
                    )
                ) {
                    Text(
                        text = "View Listings",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun AgencyStatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BalkanEstatePrimaryBlue,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun AgencySpecializationChip(
    text: String
) {
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFF0F0F0),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.Medium
        )
    }
}

// Mock data for previews
object AgencyMockData {
    val mockAgencies = listOf(
        Agency(
            id = "1",
            name = "Balkan Properties Group",
            description = "Leading real estate agency with over 20 years of experience in the Balkan market. We specialize in luxury properties, commercial real estate, and investment opportunities.",
            location = "Tirana, Albania",
            rating = 4.9f,
            reviewsCount = 312,
            agentsCount = 45,
            activeListings = 520,
            yearEstablished = 2003,
            isVerified = true,
            specializations = listOf("Luxury Homes", "Commercial", "Investment", "New Construction")
        ),
        Agency(
            id = "2",
            name = "Premier Realty Serbia",
            description = "Your trusted partner for finding the perfect property in Serbia. From city apartments to countryside estates, we have it all.",
            location = "Belgrade, Serbia",
            rating = 4.7f,
            reviewsCount = 198,
            agentsCount = 28,
            activeListings = 340,
            yearEstablished = 2008,
            isVerified = true,
            specializations = listOf("Residential", "Rentals", "Land")
        ),
        Agency(
            id = "3",
            name = "Sofia Estates",
            description = "Modern real estate solutions for the Bulgarian market. Innovative approach to buying and selling properties.",
            location = "Sofia, Bulgaria",
            rating = 4.6f,
            reviewsCount = 156,
            agentsCount = 22,
            activeListings = 280,
            yearEstablished = 2012,
            isVerified = false,
            specializations = listOf("Apartments", "Houses", "Commercial")
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun AgenciesScreenPreview() {
    BalkanEstateTheme {
        AgenciesScreen(
            agencies = AgencyMockData.mockAgencies,
            onAgencyClick = {},
            onViewListingsClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AgencyCardPreview() {
    BalkanEstateTheme {
        AgencyCard(
            agency = AgencyMockData.mockAgencies.first(),
            onClick = {},
            onViewListingsClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
