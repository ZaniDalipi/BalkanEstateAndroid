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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.agent.domain.model.Agent
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.CheckIcon
import com.zanoapps.core.presentation.designsystem.EmailIcon
import com.zanoapps.core.presentation.designsystem.KeyboardArrowDownIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon
import com.zanoapps.core.presentation.designsystem.StarIcon
import androidx.compose.ui.tooling.preview.Preview
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun AgentsScreenRoot(
    viewModel: AgentsViewModel = koinViewModel(),
    onNavigateToAgentDetail: (String) -> Unit = {}
) {
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is AgentsEvent.NavigateToAgentDetail -> onNavigateToAgentDetail(event.agentId)
            is AgentsEvent.NavigateToContactAgent -> { /* TODO: open dialer/email */ }
            is AgentsEvent.Error -> { /* TODO: show error */ }
        }
    }

    AgentsScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AgentsScreen(
    state: AgentsState,
    onAction: (AgentsAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "Top Agents",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Text(
                text = "Find the best real estate agents in the Balkans",
                style = MaterialTheme.typography.bodyMedium,
                color = BalkanEstateGray
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { onAction(AgentsAction.OnSearchQueryChanged(it)) },
                placeholder = { Text("Search agents...") },
                leadingIcon = {
                    Icon(imageVector = SaveSearchIcon, contentDescription = null, modifier = Modifier.size(20.dp))
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BalkanEstatePrimaryBlue,
                    unfocusedBorderColor = Color(0xFFE2E8F0)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${state.filteredAgents.size} agents found",
                    color = BalkanEstatePrimaryBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(state.sortOption.displayName, fontSize = 12.sp, color = Color.DarkGray)
                        Spacer(Modifier.width(4.dp))
                        Icon(KeyboardArrowDownIcon, null, Modifier.size(16.dp), tint = Color.DarkGray)
                    }
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        AgentSortOption.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.displayName) },
                                onClick = {
                                    onAction(AgentsAction.OnSortChanged(option))
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            ) {
                items(items = state.filteredAgents, key = { it.id }) { agent ->
                    AgentCard(agent = agent, onAction = onAction)
                }
            }
        }
    }
}

@Composable
private fun AgentCard(
    agent: Agent,
    onAction: (AgentsAction) -> Unit
) {
    Card(
        onClick = { onAction(AgentsAction.OnAgentClick(agent)) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(BalkanEstatePrimaryBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = agent.name.split(" ").map { it.first() }.joinToString(""),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = agent.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                        if (agent.isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = CheckIcon,
                                contentDescription = "Verified",
                                tint = BalkanEstateGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        if (agent.isPremium) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = StarIcon,
                                contentDescription = "Premium",
                                tint = BalkanEstateOrange,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(text = agent.agency, fontSize = 13.sp, color = BalkanEstateGray)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = LocationIcon, contentDescription = null, tint = BalkanEstateGray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = agent.location, fontSize = 12.sp, color = BalkanEstateGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AgentStat(value = "${agent.rating}", label = "Rating", color = BalkanEstateOrange)
                AgentStat(value = "${agent.reviewsCount}", label = "Reviews")
                AgentStat(value = "${agent.listingsCount}", label = "Listings")
                AgentStat(value = "${agent.soldCount}", label = "Sold")
                AgentStat(value = "${agent.yearsExperience}yr", label = "Exp.")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Languages
            Text(
                text = "Languages: ${agent.languages.joinToString(", ")}",
                fontSize = 12.sp,
                color = BalkanEstateGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onAction(AgentsAction.OnAgentClick(agent)) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("View Profile", fontSize = 13.sp)
                }
                Button(
                    onClick = { onAction(AgentsAction.OnContactAgent(agent)) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(EmailIcon, null, Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Contact", fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun AgentStat(value: String, label: String, color: Color = BalkanEstatePrimaryBlue) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = color)
        Text(text = label, fontSize = 11.sp, color = BalkanEstateGray)
    }
}

@Preview
@Composable
private fun AgentsScreenPreview() {
    BalkanEstateTheme {
        AgentsScreen(
            state = AgentsState(),
            onAction = {}
        )
    }
}
