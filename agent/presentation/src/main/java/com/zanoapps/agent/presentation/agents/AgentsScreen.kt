package com.zanoapps.agent.presentation.agents

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.CheckIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.StarIcon
import org.koin.androidx.compose.koinViewModel

@Composable
fun AgentsScreenRoot(
    viewModel: AgentsViewModel = koinViewModel(),
    onNavigateToAgentDetails: (String) -> Unit,
    onNavigateToAgencyDetails: (String) -> Unit
) {
    AgentsScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is AgentsAction.OnAgentClick -> onNavigateToAgentDetails(action.agent.id)
                is AgentsAction.OnAgencyClick -> onNavigateToAgencyDetails(action.agency.id)
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AgentsScreen(
    state: AgentsState,
    onAction: (AgentsAction) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        AgentsHeader()

        // Tab Row
        TabRow(
            selectedTabIndex = state.selectedTab.ordinal,
            containerColor = Color.White,
            contentColor = BalkanEstatePrimaryBlue
        ) {
            Tab(
                selected = state.selectedTab == AgentTab.AGENTS,
                onClick = { onAction(AgentsAction.OnTabSelected(AgentTab.AGENTS)) },
                text = { Text("Top Agents") }
            )
            Tab(
                selected = state.selectedTab == AgentTab.AGENCIES,
                onClick = { onAction(AgentsAction.OnTabSelected(AgentTab.AGENCIES)) },
                text = { Text("Agencies") }
            )
        }

        when (state.selectedTab) {
            AgentTab.AGENTS -> {
                if (state.isLoadingAgents) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
                    }
                } else {
                    AgentsList(
                        agents = state.agents,
                        onAgentClick = { onAction(AgentsAction.OnAgentClick(it)) },
                        onContactClick = { onAction(AgentsAction.OnContactAgent(it)) }
                    )
                }
            }
            AgentTab.AGENCIES -> {
                if (state.isLoadingAgencies) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
                    }
                } else {
                    AgenciesList(
                        agencies = state.agencies,
                        onAgencyClick = { onAction(AgentsAction.OnAgencyClick(it)) },
                        onContactClick = { onAction(AgentsAction.OnContactAgency(it)) }
                    )
                }
            }
        }
    }

    // Contact Bottom Sheet
    if (state.isContactDialogVisible) {
        ModalBottomSheet(
            onDismissRequest = { onAction(AgentsAction.OnDismissContactDialog) },
            sheetState = sheetState
        ) {
            ContactBottomSheet(
                name = state.selectedAgent?.name ?: state.selectedAgency?.name ?: "",
                phone = state.selectedAgent?.phone ?: state.selectedAgency?.phone ?: "",
                email = state.selectedAgent?.email ?: state.selectedAgency?.email ?: "",
                onCallClick = { onAction(AgentsAction.OnCallClick(it)) },
                onEmailClick = { onAction(AgentsAction.OnEmailClick(it)) }
            )
        }
    }
}

@Composable
private fun AgentsHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Find Professionals",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Connect with top-rated agents and agencies",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
private fun AgentsList(
    agents: List<Agent>,
    onAgentClick: (Agent) -> Unit,
    onContactClick: (Agent) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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

@Composable
private fun AgentCard(
    agent: Agent,
    onClick: () -> Unit,
    onContactClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(BalkanEstatePrimaryBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = agent.name.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = agent.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        if (agent.isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = CheckIcon,
                                contentDescription = "Verified",
                                tint = BalkanEstatePrimaryBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Text(
                        text = agent.agency,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Text(
                        text = agent.specialization,
                        color = BalkanEstatePrimaryBlue,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = BalkanEstateOrange,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${agent.rating}",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        Text(
                            text = " (${agent.reviewCount} reviews)",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(value = "${agent.listingsCount}", label = "Listings")
                StatItem(value = "${agent.soldCount}", label = "Sold")
                StatItem(value = "${agent.yearsExperience} yrs", label = "Experience")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Contact Button
            Button(
                onClick = onContactClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Contact Agent")
            }
        }
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = BalkanEstatePrimaryBlue
        )
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun AgenciesList(
    agencies: List<Agency>,
    onAgencyClick: (Agency) -> Unit,
    onContactClick: (Agency) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = agencies,
            key = { it.id }
        ) { agency ->
            AgencyCard(
                agency = agency,
                onClick = { onAgencyClick(agency) },
                onContactClick = { onContactClick(agency) }
            )
        }
    }
}

@Composable
private fun AgencyCard(
    agency: Agency,
    onClick: () -> Unit,
    onContactClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Logo placeholder
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = agency.name.take(2).uppercase(),
                        color = BalkanEstatePrimaryBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = agency.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        if (agency.isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = CheckIcon,
                                contentDescription = "Verified",
                                tint = BalkanEstatePrimaryBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = LocationIcon,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${agency.address}, ${agency.city}",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = BalkanEstateOrange,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${agency.rating}",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        Text(
                            text = " (${agency.reviewCount} reviews)",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(value = "${agency.agentCount}", label = "Agents")
                StatItem(value = "${agency.listingsCount}", label = "Listings")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Contact Button
            Button(
                onClick = onContactClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Contact Agency")
            }
        }
    }
}

@Composable
private fun ContactBottomSheet(
    name: String,
    phone: String,
    email: String,
    onCallClick: (String) -> Unit,
    onEmailClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Contact $name",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onCallClick(phone) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.Call, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Call $phone")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { onEmailClick(email) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = BalkanEstatePrimaryBlue
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Send Email", color = BalkanEstatePrimaryBlue)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun AgentsScreenPreview() {
    BalkanEstateTheme {
        AgentsScreen(
            state = AgentsState(
                agents = listOf(
                    Agent(
                        id = "1",
                        name = "Marina Lleshi",
                        avatarUrl = null,
                        agency = "Balkan Estate",
                        specialization = "Luxury Properties",
                        rating = 4.9f,
                        reviewCount = 127,
                        listingsCount = 45,
                        soldCount = 89,
                        phone = "+355 69 111 2222",
                        email = "marina@balkanestate.com",
                        isVerified = true,
                        yearsExperience = 12
                    )
                ),
                isLoadingAgents = false,
                selectedTab = AgentTab.AGENTS
            ),
            onAction = {}
        )
    }
}
