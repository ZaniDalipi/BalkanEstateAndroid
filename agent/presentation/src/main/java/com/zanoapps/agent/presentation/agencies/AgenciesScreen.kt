package com.zanoapps.agent.presentation.agencies

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.agent.domain.model.Agency
import com.zanoapps.core.presentation.designsystem.AgencyIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.CheckIcon
import com.zanoapps.core.presentation.designsystem.EmailIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon
import com.zanoapps.core.presentation.designsystem.StarIcon
import org.koin.androidx.compose.koinViewModel

@Composable
fun AgenciesScreenRoot(
    viewModel: AgenciesViewModel = koinViewModel()
) {
    AgenciesScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun AgenciesScreen(
    state: AgenciesState,
    onAction: (AgenciesAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "Agencies",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Text(
                text = "Browse trusted real estate agencies",
                style = MaterialTheme.typography.bodyMedium,
                color = BalkanEstateGray
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { onAction(AgenciesAction.OnSearchQueryChanged(it)) },
                placeholder = { Text("Search agencies...") },
                leadingIcon = { Icon(SaveSearchIcon, null, Modifier.size(20.dp)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BalkanEstatePrimaryBlue,
                    unfocusedBorderColor = Color(0xFFE2E8F0)
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${state.filteredAgencies.size} agencies found",
                color = BalkanEstatePrimaryBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
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
                items(items = state.filteredAgencies, key = { it.id }) { agency ->
                    AgencyCard(agency = agency, onAction = onAction)
                }
            }
        }
    }
}

@Composable
private fun AgencyCard(agency: Agency, onAction: (AgenciesAction) -> Unit) {
    Card(
        onClick = { onAction(AgenciesAction.OnAgencyClick(agency)) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(52.dp).clip(CircleShape).background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(AgencyIcon, null, Modifier.size(28.dp), tint = BalkanEstatePrimaryBlue)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(agency.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                        if (agency.isVerified) {
                            Spacer(Modifier.width(4.dp))
                            Icon(CheckIcon, "Verified", tint = BalkanEstateGreen, modifier = Modifier.size(16.dp))
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(LocationIcon, null, tint = BalkanEstateGray, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(2.dp))
                        Text("${agency.city}, ${agency.country}", fontSize = 13.sp, color = BalkanEstateGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(StarIcon, null, tint = BalkanEstateOrange, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(2.dp))
                        Text("${agency.rating}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BalkanEstateOrange)
                    }
                    Text("${agency.reviewsCount} reviews", fontSize = 11.sp, color = BalkanEstateGray)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(PersonIcon, null, tint = BalkanEstatePrimaryBlue, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(2.dp))
                        Text("${agency.agentsCount}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BalkanEstatePrimaryBlue)
                    }
                    Text("Agents", fontSize = 11.sp, color = BalkanEstateGray)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${agency.listingsCount}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BalkanEstatePrimaryBlue)
                    Text("Listings", fontSize = 11.sp, color = BalkanEstateGray)
                }
            }

            if (agency.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(agency.description, fontSize = 13.sp, color = BalkanEstateGray, lineHeight = 18.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { onAction(AgenciesAction.OnAgencyClick(agency)) }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp)) {
                    Text("View Details", fontSize = 13.sp)
                }
                Button(onClick = { onAction(AgenciesAction.OnContactAgency(agency)) }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue), shape = RoundedCornerShape(8.dp)) {
                    Icon(EmailIcon, null, Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Contact", fontSize = 13.sp)
                }
            }
        }
    }
}
