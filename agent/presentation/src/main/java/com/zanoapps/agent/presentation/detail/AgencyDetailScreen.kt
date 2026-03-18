package com.zanoapps.agent.presentation.detail

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.agent.domain.model.Agency
import com.zanoapps.core.presentation.designsystem.AgencyIcon
import com.zanoapps.core.presentation.designsystem.BackIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.CheckIcon
import com.zanoapps.core.presentation.designsystem.EmailIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.StarIcon
import org.koin.androidx.compose.koinViewModel

@Composable
fun AgencyDetailScreenRoot(
    viewModel: AgencyDetailViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    AgencyDetailScreen(
        state = viewModel.state,
        onAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AgencyDetailScreen(
    state: AgencyDetailState,
    onAction: (AgencyDetailAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onAction(AgencyDetailAction.OnBackClick) }) {
                Icon(
                    imageVector = BackIcon,
                    contentDescription = "Back",
                    tint = Color.DarkGray
                )
            }
            Text(
                text = "Agency Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
        }

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
                }
            }

            state.errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.errorMessage,
                            color = BalkanEstateGray,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onAction(AgencyDetailAction.OnRetry) },
                            colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }

            state.agency != null -> {
                val agency = state.agency
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Agency Header Card
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Logo placeholder
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = AgencyIcon,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = BalkanEstatePrimaryBlue
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Name with verification badge
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = agency.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Color.DarkGray
                                )
                                if (agency.isVerified) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = CheckIcon,
                                        contentDescription = "Verified",
                                        tint = BalkanEstateGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Location
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = LocationIcon,
                                    contentDescription = null,
                                    tint = BalkanEstateGray,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = buildString {
                                        if (agency.city.isNotBlank()) append(agency.city)
                                        if (agency.city.isNotBlank() && agency.country.isNotBlank()) append(", ")
                                        if (agency.country.isNotBlank()) append(agency.country)
                                    },
                                    fontSize = 13.sp,
                                    color = BalkanEstateGray
                                )
                            }

                            if (agency.address.isNotBlank()) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = agency.address,
                                    fontSize = 12.sp,
                                    color = BalkanEstateGray
                                )
                            }
                        }
                    }

                    // Stats Card
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Statistics",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                AgencyStatItem(
                                    icon = {
                                        Icon(
                                            imageVector = StarIcon,
                                            contentDescription = null,
                                            tint = BalkanEstateOrange,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    },
                                    value = "${agency.rating}",
                                    label = "Rating",
                                    color = BalkanEstateOrange
                                )
                                AgencyStatItem(
                                    value = "${agency.reviewsCount}",
                                    label = "Reviews"
                                )
                                AgencyStatItem(
                                    icon = {
                                        Icon(
                                            imageVector = PersonIcon,
                                            contentDescription = null,
                                            tint = BalkanEstatePrimaryBlue,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    },
                                    value = "${agency.agentsCount}",
                                    label = "Agents"
                                )
                                AgencyStatItem(
                                    value = "${agency.listingsCount}",
                                    label = "Listings"
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Description Card
                    if (agency.description.isNotBlank()) {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "About",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = agency.description,
                                    fontSize = 14.sp,
                                    color = BalkanEstateGray,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Contact Info Card
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Contact Information",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            if (agency.phone.isNotBlank()) {
                                ContactInfoRow(
                                    icon = {
                                        Icon(
                                            imageVector = PersonIcon,
                                            contentDescription = null,
                                            tint = BalkanEstatePrimaryBlue,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    },
                                    label = "Phone",
                                    value = agency.phone
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            if (agency.email.isNotBlank()) {
                                ContactInfoRow(
                                    icon = {
                                        Icon(
                                            imageVector = EmailIcon,
                                            contentDescription = null,
                                            tint = BalkanEstatePrimaryBlue,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    },
                                    label = "Email",
                                    value = agency.email
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            if (agency.website.isNotBlank()) {
                                ContactInfoRow(
                                    icon = {
                                        Icon(
                                            imageVector = SaveSearchIcon,
                                            contentDescription = null,
                                            tint = BalkanEstatePrimaryBlue,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    },
                                    label = "Website",
                                    value = agency.website
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Contact buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { onAction(AgencyDetailAction.OnCallClick) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = PersonIcon,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text("Call", fontSize = 13.sp)
                                }
                                OutlinedButton(
                                    onClick = { onAction(AgencyDetailAction.OnEmailClick) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = EmailIcon,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text("Email", fontSize = 13.sp)
                                }
                                OutlinedButton(
                                    onClick = { onAction(AgencyDetailAction.OnWebsiteClick) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = SaveSearchIcon,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text("Web", fontSize = 13.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun AgencyStatItem(
    value: String,
    label: String,
    color: Color = BalkanEstatePrimaryBlue,
    icon: (@Composable () -> Unit)? = null
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(2.dp))
            }
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = color
            )
        }
        Text(
            text = label,
            fontSize = 11.sp,
            color = BalkanEstateGray
        )
    }
}

@Composable
private fun ContactInfoRow(
    icon: @Composable () -> Unit,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        icon()
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = BalkanEstateGray
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AgencyDetailScreenPreview() {
    BalkanEstateTheme {
        AgencyDetailScreen(
            state = AgencyDetailState(
                agency = Agency(
                    id = "1",
                    name = "Balkan Realty Group",
                    logoUrl = "",
                    address = "Ilica 42",
                    city = "Zagreb",
                    country = "Croatia",
                    phone = "+385 1 234 5678",
                    email = "info@balkanrealty.com",
                    website = "www.balkanrealty.com",
                    rating = 4.7f,
                    reviewsCount = 256,
                    agentsCount = 18,
                    listingsCount = 342,
                    description = "Leading real estate agency in the Balkans with over 15 years of experience. We specialize in residential and commercial properties across Croatia, Serbia, and Montenegro.",
                    isVerified = true
                )
            ),
            onAction = {}
        )
    }
}
