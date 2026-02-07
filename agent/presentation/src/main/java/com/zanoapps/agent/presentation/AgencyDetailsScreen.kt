package com.zanoapps.agent.presentation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.BuildingIcon
import com.zanoapps.core.presentation.designsystem.CalendarIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.MailIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.PhoneIcon
import com.zanoapps.core.presentation.designsystem.Poppins
import com.zanoapps.core.presentation.designsystem.RatingStarIcon
import com.zanoapps.core.presentation.designsystem.VerifiedIcon
import com.zanoapps.core.presentation.designsystem.components.MockData
import com.zanoapps.core.presentation.designsystem.components.PropertyCard

data class AgencyDetailsData(
    val id: String,
    val name: String,
    val logoUrl: String?,
    val location: String,
    val phone: String,
    val email: String,
    val website: String,
    val rating: Float,
    val reviewCount: Int,
    val listingsCount: Int,
    val agentsCount: Int,
    val yearsInBusiness: Int,
    val description: String,
    val services: List<String>,
    val coverageAreas: List<String>,
    val isVerified: Boolean,
    val agents: List<AgentCardData>,
    val featuredListings: List<BalkanEstateProperty>
)

data class AgentCardData(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val title: String,
    val rating: Float,
    val listingsCount: Int,
    val isVerified: Boolean
)

@Composable
fun AgencyDetailsScreenRoot(
    agency: AgencyDetailsData,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onCallClick: () -> Unit,
    onEmailClick: () -> Unit,
    onWebsiteClick: () -> Unit,
    onAgentClick: (AgentCardData) -> Unit,
    onPropertyClick: (BalkanEstateProperty) -> Unit,
    onViewAllListingsClick: () -> Unit,
    onViewAllAgentsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AgencyDetailsScreen(
        agency = agency,
        onBackClick = onBackClick,
        onShareClick = onShareClick,
        onCallClick = onCallClick,
        onEmailClick = onEmailClick,
        onWebsiteClick = onWebsiteClick,
        onAgentClick = onAgentClick,
        onPropertyClick = onPropertyClick,
        onViewAllListingsClick = onViewAllListingsClick,
        onViewAllAgentsClick = onViewAllAgentsClick,
        modifier = modifier
    )
}

@Composable
fun AgencyDetailsScreen(
    agency: AgencyDetailsData,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onCallClick: () -> Unit,
    onEmailClick: () -> Unit,
    onWebsiteClick: () -> Unit,
    onAgentClick: (AgentCardData) -> Unit,
    onPropertyClick: (BalkanEstateProperty) -> Unit,
    onViewAllListingsClick: () -> Unit,
    onViewAllAgentsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.DarkGray
                            )
                        }

                        Text(
                            text = "Agency Profile",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray,
                            fontFamily = Poppins
                        )

                        IconButton(onClick = onShareClick) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.DarkGray
                            )
                        }
                    }
                }
            }

            // Agency Profile Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Logo
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = BuildingIcon,
                                contentDescription = null,
                                tint = BalkanEstatePrimaryBlue,
                                modifier = Modifier.size(50.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Name with verification
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = agency.name,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray,
                                fontFamily = Poppins
                            )
                            if (agency.isVerified) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = VerifiedIcon,
                                    contentDescription = "Verified",
                                    tint = BalkanEstateGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Location
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = LocationIcon,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = agency.location,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Established
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = CalendarIcon,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Est. ${2024 - agency.yearsInBusiness}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Rating
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(5) { index ->
                                Icon(
                                    imageVector = RatingStarIcon,
                                    contentDescription = null,
                                    tint = if (index < agency.rating.toInt())
                                        BalkanEstateOrange
                                    else
                                        Color.LightGray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${agency.rating}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray
                            )
                            Text(
                                text = " (${agency.reviewCount} reviews)",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Stats
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            AgencyStatItem(value = agency.listingsCount.toString(), label = "Listings")
                            AgencyStatItem(value = agency.agentsCount.toString(), label = "Agents")
                            AgencyStatItem(value = "${agency.yearsInBusiness}+", label = "Years")
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Contact Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = onCallClick,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = BalkanEstatePrimaryBlue
                                )
                            ) {
                                Icon(
                                    imageVector = PhoneIcon,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Call",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Button(
                                onClick = onEmailClick,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BalkanEstatePrimaryBlue
                                )
                            ) {
                                Icon(
                                    imageVector = MailIcon,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Email",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            // About Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "About",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = agency.description,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                        Spacer(modifier = Modifier.height(16.dp))

                        // Services
                        Text(
                            text = "Services",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            agency.services.forEach { service ->
                                Text(
                                    text = "• $service",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Coverage Areas
                        Text(
                            text = "Coverage Areas",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(agency.coverageAreas) { area ->
                                CoverageAreaChip(text = area)
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Our Agents Section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Our Agents (${agency.agentsCount})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )

                    Text(
                        text = "View All",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = BalkanEstatePrimaryBlue,
                        modifier = Modifier
                            .clickable { onViewAllAgentsClick() }
                            .padding(8.dp)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Agents Row
            item {
                LazyRow(
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(agency.agents) { agent ->
                        AgentMiniCard(
                            agent = agent,
                            onClick = { onAgentClick(agent) }
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Featured Listings Section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Featured Listings",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )

                    Text(
                        text = "View All",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = BalkanEstatePrimaryBlue,
                        modifier = Modifier
                            .clickable { onViewAllListingsClick() }
                            .padding(8.dp)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Property Listings
            items(agency.featuredListings.take(3)) { property ->
                PropertyCard(
                    property = property,
                    isFavorite = false,
                    onFavoriteClick = {},
                    onClick = { onPropertyClick(property) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
private fun AgencyStatItem(
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun CoverageAreaChip(text: String) {
    Box(
        modifier = Modifier
            .background(
                BalkanEstatePrimaryBlue.copy(alpha = 0.1f),
                RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            color = BalkanEstatePrimaryBlue,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun AgentMiniCard(
    agent: AgentCardData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = PersonIcon,
                    contentDescription = null,
                    tint = BalkanEstatePrimaryBlue,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = agent.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray,
                    maxLines = 1
                )
                if (agent.isVerified) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = VerifiedIcon,
                        contentDescription = null,
                        tint = BalkanEstateGreen,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Text(
                text = agent.title,
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = RatingStarIcon,
                    contentDescription = null,
                    tint = BalkanEstateOrange,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${agent.rating}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                )
            }
        }
    }
}

// Mock data
object AgencyDetailsMockData {
    val mockAgencyDetails = AgencyDetailsData(
        id = "1",
        name = "Balkan Properties Group",
        logoUrl = null,
        location = "Tirana, Albania",
        phone = "+355 4 234 5678",
        email = "info@balkanproperties.com",
        website = "www.balkanproperties.com",
        rating = 4.9f,
        reviewCount = 324,
        listingsCount = 156,
        agentsCount = 12,
        yearsInBusiness = 15,
        description = "Balkan Properties Group is a leading real estate agency serving " +
                "the Balkans since 2009. We specialize in residential and commercial " +
                "properties across Albania, Kosovo, and North Macedonia. Our dedicated " +
                "team of experienced agents is committed to helping you find your " +
                "perfect property.",
        services = listOf(
            "Property Sales & Purchases",
            "Rental Services",
            "Property Management",
            "Investment Consulting",
            "Legal Assistance"
        ),
        coverageAreas = listOf("Tirana", "Durres", "Vlora", "Saranda", "Shkoder"),
        isVerified = true,
        agents = listOf(
            AgentCardData("1", "John Doe", null, "Senior Agent", 4.8f, 45, true),
            AgentCardData("2", "Maria Smith", null, "Agent", 4.6f, 28, true),
            AgentCardData("3", "Alex Brown", null, "Junior Agent", 4.4f, 12, false)
        ),
        featuredListings = MockData.getMockProperties()
    )
}

@Preview(showBackground = true)
@Composable
private fun AgencyDetailsScreenPreview() {
    BalkanEstateTheme {
        AgencyDetailsScreen(
            agency = AgencyDetailsMockData.mockAgencyDetails,
            onBackClick = {},
            onShareClick = {},
            onCallClick = {},
            onEmailClick = {},
            onWebsiteClick = {},
            onAgentClick = {},
            onPropertyClick = {},
            onViewAllListingsClick = {},
            onViewAllAgentsClick = {}
        )
    }
}
