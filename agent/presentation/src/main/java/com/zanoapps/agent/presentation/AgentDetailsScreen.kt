package com.zanoapps.agent.presentation

import androidx.compose.foundation.background
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

data class AgentDetails(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val agency: String,
    val location: String,
    val phone: String,
    val email: String,
    val rating: Float,
    val reviewCount: Int,
    val listingsCount: Int,
    val soldCount: Int,
    val yearsExperience: Int,
    val specializations: List<String>,
    val languages: List<String>,
    val bio: String,
    val isVerified: Boolean,
    val listings: List<BalkanEstateProperty>
)

@Composable
fun AgentDetailsScreenRoot(
    agent: AgentDetails,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onCallClick: () -> Unit,
    onMessageClick: () -> Unit,
    onPropertyClick: (BalkanEstateProperty) -> Unit,
    onViewAllListingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AgentDetailsScreen(
        agent = agent,
        onBackClick = onBackClick,
        onShareClick = onShareClick,
        onCallClick = onCallClick,
        onMessageClick = onMessageClick,
        onPropertyClick = onPropertyClick,
        onViewAllListingsClick = onViewAllListingsClick,
        modifier = modifier
    )
}

@Composable
fun AgentDetailsScreen(
    agent: AgentDetails,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onCallClick: () -> Unit,
    onMessageClick: () -> Unit,
    onPropertyClick: (BalkanEstateProperty) -> Unit,
    onViewAllListingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header with back button
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
                            text = "Agent Profile",
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

            // Agent Profile Card
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
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = PersonIcon,
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
                                text = agent.name,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray,
                                fontFamily = Poppins
                            )
                            if (agent.isVerified) {
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

                        // Agency
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = BuildingIcon,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = agent.agency,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
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
                                text = agent.location,
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
                                    tint = if (index < agent.rating.toInt())
                                        BalkanEstateOrange
                                    else
                                        Color.LightGray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${agent.rating}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray
                            )
                            Text(
                                text = " (${agent.reviewCount} reviews)",
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
                            StatItem(value = agent.listingsCount.toString(), label = "Listings")
                            StatItem(value = agent.soldCount.toString(), label = "Sold")
                            StatItem(value = "${agent.yearsExperience}+", label = "Years")
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
                                onClick = onMessageClick,
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
                                    text = "Message",
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
                            text = agent.bio,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                        Spacer(modifier = Modifier.height(16.dp))

                        // Specializations
                        Text(
                            text = "Specializations",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(agent.specializations) { spec ->
                                SpecializationChip(text = spec)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Languages
                        Text(
                            text = "Languages",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = agent.languages.joinToString(", "),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Listings Section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Active Listings (${agent.listingsCount})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )

                    Text(
                        text = "View All",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = BalkanEstatePrimaryBlue,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Property Listings
            items(agent.listings.take(3)) { property ->
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
private fun StatItem(
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
private fun SpecializationChip(text: String) {
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

// Mock data for preview
object AgentDetailsMockData {
    val mockAgentDetails = AgentDetails(
        id = "1",
        name = "John Doe",
        avatarUrl = null,
        agency = "Balkan Properties Group",
        location = "Tirana, Albania",
        phone = "+355 69 123 4567",
        email = "john.doe@balkanproperties.com",
        rating = 4.8f,
        reviewCount = 127,
        listingsCount = 45,
        soldCount = 89,
        yearsExperience = 8,
        specializations = listOf("Luxury Villas", "Apartments", "Commercial"),
        languages = listOf("English", "Albanian", "Italian"),
        bio = "With over 8 years of experience in the Balkan real estate market, " +
                "I specialize in helping clients find their dream properties. " +
                "Whether you're looking for a luxury villa on the coast or a " +
                "modern apartment in the city center, I'm here to guide you through " +
                "every step of the process.",
        isVerified = true,
        listings = MockData.getMockProperties()
    )
}

@Preview(showBackground = true)
@Composable
private fun AgentDetailsScreenPreview() {
    BalkanEstateTheme {
        AgentDetailsScreen(
            agent = AgentDetailsMockData.mockAgentDetails,
            onBackClick = {},
            onShareClick = {},
            onCallClick = {},
            onMessageClick = {},
            onPropertyClick = {},
            onViewAllListingsClick = {}
        )
    }
}
