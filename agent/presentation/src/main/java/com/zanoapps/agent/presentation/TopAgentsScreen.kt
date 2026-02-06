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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.EmailIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.PhoneIcon
import com.zanoapps.core.presentation.designsystem.Poppins
import com.zanoapps.core.presentation.designsystem.RatingStarIcon
import com.zanoapps.core.presentation.designsystem.VerifiedIcon

data class Agent(
    val id: String,
    val name: String,
    val title: String,
    val agency: String,
    val location: String,
    val rating: Float,
    val reviewsCount: Int,
    val propertiesSold: Int,
    val yearsExperience: Int,
    val isVerified: Boolean = false,
    val specializations: List<String> = emptyList(),
    val avatarInitial: String = name.take(1).uppercase()
)

@Composable
fun TopAgentsScreenRoot(
    agents: List<Agent>,
    onAgentClick: (Agent) -> Unit,
    onContactClick: (Agent) -> Unit,
    modifier: Modifier = Modifier
) {
    TopAgentsScreen(
        agents = agents,
        onAgentClick = onAgentClick,
        onContactClick = onContactClick,
        modifier = modifier
    )
}

@Composable
fun TopAgentsScreen(
    agents: List<Agent>,
    onAgentClick: (Agent) -> Unit,
    onContactClick: (Agent) -> Unit,
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
                        text = "Top Agents",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        fontFamily = Poppins
                    )
                    Text(
                        text = "Find the best real estate professionals in the Balkans",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // Agents List
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = agents,
                    key = { it.id }
                ) { agent ->
                    AgentCard(
                        agent = agent,
                        onClick = { onAgentClick(agent) },
                        onContactClick = { onContactClick(agent) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AgentCard(
    agent: Agent,
    onClick: () -> Unit,
    onContactClick: () -> Unit,
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
                // Avatar
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(BalkanEstatePrimaryBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = agent.avatarInitial,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // Name with verified badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = agent.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                        if (agent.isVerified) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = VerifiedIcon,
                                contentDescription = "Verified",
                                tint = BalkanEstatePrimaryBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Text(
                        text = agent.title,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = agent.agency,
                        fontSize = 14.sp,
                        color = BalkanEstatePrimaryBlue,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = RatingStarIcon,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", agent.rating),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray
                        )
                        Text(
                            text = " (${agent.reviewsCount} reviews)",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(value = "${agent.propertiesSold}+", label = "Properties Sold")
                StatItem(value = "${agent.yearsExperience} yrs", label = "Experience")
            }

            Spacer(modifier = Modifier.height(12.dp))

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
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = agent.location,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // Specializations
            if (agent.specializations.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    agent.specializations.take(3).forEach { spec ->
                        SpecializationChip(text = spec)
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
                        text = "View Profile",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Button(
                    onClick = onContactClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BalkanEstatePrimaryBlue
                    )
                ) {
                    Icon(
                        imageVector = EmailIcon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Contact",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
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
            fontSize = 18.sp,
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
private fun SpecializationChip(
    text: String
) {
    Box(
        modifier = Modifier
            .background(
                color = BalkanEstatePrimaryBlue.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = BalkanEstatePrimaryBlue,
            fontWeight = FontWeight.Medium
        )
    }
}

// Mock data for previews
object AgentMockData {
    val mockAgents = listOf(
        Agent(
            id = "1",
            name = "Elena Petrov",
            title = "Senior Real Estate Agent",
            agency = "Balkan Properties Group",
            location = "Tirana, Albania",
            rating = 4.9f,
            reviewsCount = 127,
            propertiesSold = 250,
            yearsExperience = 12,
            isVerified = true,
            specializations = listOf("Luxury Homes", "Commercial", "Investment")
        ),
        Agent(
            id = "2",
            name = "Marko Jovanovic",
            title = "Property Consultant",
            agency = "Premier Realty",
            location = "Belgrade, Serbia",
            rating = 4.8f,
            reviewsCount = 89,
            propertiesSold = 180,
            yearsExperience = 8,
            isVerified = true,
            specializations = listOf("Residential", "New Construction")
        ),
        Agent(
            id = "3",
            name = "Ana Dimitrova",
            title = "Real Estate Specialist",
            agency = "Sofia Estates",
            location = "Sofia, Bulgaria",
            rating = 4.7f,
            reviewsCount = 64,
            propertiesSold = 120,
            yearsExperience = 6,
            isVerified = false,
            specializations = listOf("Apartments", "Rentals")
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun TopAgentsScreenPreview() {
    BalkanEstateTheme {
        TopAgentsScreen(
            agents = AgentMockData.mockAgents,
            onAgentClick = {},
            onContactClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AgentCardPreview() {
    BalkanEstateTheme {
        AgentCard(
            agent = AgentMockData.mockAgents.first(),
            onClick = {},
            onContactClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
