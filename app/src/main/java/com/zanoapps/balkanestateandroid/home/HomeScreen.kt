package com.zanoapps.balkanestateandroid.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.agent.domain.model.Agent
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.presentation.designsystem.BalkanEstateBackground
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryGradient
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.CheckIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.Poppins
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon
import com.zanoapps.core.presentation.designsystem.StarIcon
import com.zanoapps.core.presentation.designsystem.components.EmailSubscriptionBar
import com.zanoapps.core.presentation.designsystem.components.PropertyCard
import com.zanoapps.presentation.ui.ObserveAsEvents
import com.zanoapps.ads.domain.model.AdPlacement
import com.zanoapps.ads.presentation.ads.AdBannerRoot
import org.koin.androidx.compose.koinViewModel

// -----------------------------------------------------------------
// Data models for sections
// -----------------------------------------------------------------

private data class BalkanCountry(
    val name: String,
    val flag: String,
    val propertyCount: Int
)

private data class PropertyType(
    val name: String,
    val emoji: String
)

private data class WhyChooseItem(
    val title: String,
    val subtitle: String,
    val emoji: String
)

private val balkanCountries = listOf(
    BalkanCountry("Albania", "\uD83C\uDDE6\uD83C\uDDF1", 1_240),
    BalkanCountry("Kosovo", "\uD83C\uDDFD\uD83C\uDDF0", 860),
    BalkanCountry("Montenegro", "\uD83C\uDDF2\uD83C\uDDEA", 720),
    BalkanCountry("Serbia", "\uD83C\uDDF7\uD83C\uDDF8", 2_150),
    BalkanCountry("North Macedonia", "\uD83C\uDDF2\uD83C\uDDF0", 640),
    BalkanCountry("Croatia", "\uD83C\uDDED\uD83C\uDDF7", 3_420),
    BalkanCountry("Bosnia", "\uD83C\uDDE7\uD83C\uDDE6", 980),
    BalkanCountry("Greece", "\uD83C\uDDEC\uD83C\uDDF7", 5_100),
    BalkanCountry("Bulgaria", "\uD83C\uDDE7\uD83C\uDDEC", 2_870),
    BalkanCountry("Romania", "\uD83C\uDDF7\uD83C\uDDF4", 3_950),
    BalkanCountry("Slovenia", "\uD83C\uDDF8\uD83C\uDDEE", 1_080)
)

private val popularCities = listOf(
    "Tirana", "Pristina", "Podgorica", "Belgrade", "Skopje",
    "Zagreb", "Sarajevo", "Athens", "Sofia", "Bucharest"
)

private val propertyTypes = listOf(
    PropertyType("Apartments", "\uD83C\uDFE2"),
    PropertyType("Houses", "\uD83C\uDFE0"),
    PropertyType("Villas", "\uD83C\uDFD6\uFE0F"),
    PropertyType("Commercial", "\uD83C\uDFEC"),
    PropertyType("Land", "\uD83C\uDF33"),
    PropertyType("Studios", "\uD83D\uDECB\uFE0F")
)

private val whyChooseItems = listOf(
    WhyChooseItem("AI-Powered Search", "Smart property matching", "\uD83E\uDDE0"),
    WhyChooseItem("10 Languages", "Multilingual support", "\uD83C\uDF10"),
    WhyChooseItem("11 Countries", "Full Balkan coverage", "\uD83C\uDDEA\uD83C\uDDFA"),
    WhyChooseItem("Verified Listings", "Trusted properties", "\u2705")
)

// -----------------------------------------------------------------
// Navigation callback interface
// -----------------------------------------------------------------

interface HomeNavigationCallback {
    fun onNavigateToSearch(query: String)
    fun onNavigateToPropertyDetail(propertyId: String)
    fun onNavigateToCountry(country: String)
    fun onNavigateToCity(city: String)
    fun onNavigateToAgentDetail(agentId: String)
    fun onNavigateToAllProperties()
    fun onNavigateToAllAgents()
    fun onNavigateToPropertyType(type: String)
}

// -----------------------------------------------------------------
// Root composable
// -----------------------------------------------------------------

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel(),
    navigationCallback: HomeNavigationCallback? = null
) {
    ObserveAsEvents(events = viewModel.events) { event ->
        when (event) {
            is HomeEvent.NavigateToSearch -> navigationCallback?.onNavigateToSearch(event.query)
            is HomeEvent.NavigateToPropertyDetail -> navigationCallback?.onNavigateToPropertyDetail(event.propertyId)
            is HomeEvent.NavigateToCountry -> navigationCallback?.onNavigateToCountry(event.country)
            is HomeEvent.NavigateToCity -> navigationCallback?.onNavigateToCity(event.city)
            is HomeEvent.NavigateToAgentDetail -> navigationCallback?.onNavigateToAgentDetail(event.agentId)
            HomeEvent.NavigateToAllProperties -> navigationCallback?.onNavigateToAllProperties()
            HomeEvent.NavigateToAllAgents -> navigationCallback?.onNavigateToAllAgents()
            is HomeEvent.NavigateToPropertyType -> navigationCallback?.onNavigateToPropertyType(event.type)
            HomeEvent.SubscriptionSuccess -> { /* Show toast from caller */ }
            is HomeEvent.SubscriptionError -> { /* Show toast from caller */ }
        }
    }

    HomeScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

// -----------------------------------------------------------------
// Main HomeScreen
// -----------------------------------------------------------------

@Composable
private fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BalkanEstateBackground),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. Hero Section
        item {
            HeroSection(
                searchQuery = state.searchQuery,
                onSearchQueryChanged = { onAction(HomeAction.OnSearchQueryChanged(it)) },
                onSearchClick = { onAction(HomeAction.OnSearchClick) },
                onBrowseClick = { onAction(HomeAction.OnViewAllProperties) }
            )
        }

        // 2. Featured Properties Section
        item {
            FeaturedPropertiesSection(
                properties = state.featuredProperties,
                isLoading = state.isLoadingProperties,
                onPropertyClick = { onAction(HomeAction.OnPropertyClick(it.id)) },
                onSeeAllClick = { onAction(HomeAction.OnViewAllProperties) }
            )
        }

        // 3. Browse by Country Section
        item {
            BrowseByCountrySection(
                onCountryClick = { onAction(HomeAction.OnCountryClick(it)) }
            )
        }

        // 4. Browse by City Section
        item {
            BrowseByCitySection(
                onCityClick = { onAction(HomeAction.OnCityClick(it)) }
            )
        }

        // Ad Banner
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                AdBannerRoot(placement = AdPlacement.HOME_FEED)
            }
        }

        // 5. Property Types Section
        item {
            PropertyTypesSection(
                onPropertyTypeClick = { onAction(HomeAction.OnPropertyTypeClick(it)) }
            )
        }

        // 6. Top Agents Section
        item {
            TopAgentsSection(
                agents = state.topAgents,
                isLoading = state.isLoadingAgents,
                onAgentClick = { onAction(HomeAction.OnAgentClick(it)) },
                onSeeAllClick = { onAction(HomeAction.OnViewAllAgents) }
            )
        }

        // 7. Why Choose Us Section
        item {
            WhyChooseUsSection()
        }

        // 8. Email Subscription Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            EmailSubscriptionBar(
                onSubscribe = { email -> onAction(HomeAction.OnSubscribeEmail(email)) }
            )
        }
    }
}

// -----------------------------------------------------------------
// 1. Hero Section
// -----------------------------------------------------------------

@Composable
private fun HeroSection(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchClick: () -> Unit,
    onBrowseClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(BalkanEstatePrimaryGradient)
            .padding(horizontal = 20.dp, vertical = 32.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Find Your Dream Home in the Balkans",
                style = TextStyle(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    lineHeight = 34.sp,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "AI-powered property search across 11 Balkan countries",
                style = TextStyle(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.85f)
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Search bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = SaveSearchIcon,
                    contentDescription = null,
                    tint = BalkanEstateGray,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(20.dp)
                )
                BasicTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    textStyle = TextStyle(
                        fontFamily = Poppins,
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search by city, country, or keyword...",
                                color = BalkanEstateGray,
                                fontSize = 14.sp,
                                fontFamily = Poppins
                            )
                        }
                        innerTextField()
                    }
                )
                Button(
                    onClick = onSearchClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BalkanEstatePrimaryBlue
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(
                        text = "Search",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Browse Properties button
            Button(
                onClick = onBrowseClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BalkanEstateOrange
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Browse Properties",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
        }
    }
}

// -----------------------------------------------------------------
// 2. Featured Properties Section
// -----------------------------------------------------------------

@Composable
private fun FeaturedPropertiesSection(
    properties: List<BalkanEstateProperty>,
    isLoading: Boolean,
    onPropertyClick: (BalkanEstateProperty) -> Unit,
    onSeeAllClick: () -> Unit
) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        SectionHeader(
            title = "Featured",
            actionLabel = "See All",
            onActionClick = onSeeAllClick
        )

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
            }
        } else if (properties.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No featured properties yet",
                    color = BalkanEstateGray,
                    fontFamily = Poppins,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = properties, key = { it.id }) { property ->
                    Box(modifier = Modifier.width(300.dp)) {
                        PropertyCard(
                            property = property,
                            isFavorite = false,
                            isNew = property.isFeatured,
                            onPropertyClick = { onPropertyClick(it) },
                            onViewDetailsClick = { onPropertyClick(it) }
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------
// 3. Browse by Country Section
// -----------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BrowseByCountrySection(
    onCountryClick: (String) -> Unit
) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        SectionHeader(title = "Browse by Country")

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            balkanCountries.forEach { country ->
                CountryCard(
                    country = country,
                    onClick = { onCountryClick(country.name) }
                )
            }
        }
    }
}

@Composable
private fun CountryCard(
    country: BalkanCountry,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(105.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = country.flag,
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = country.name,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color.DarkGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Text(
                text = "${country.propertyCount} listings",
                fontFamily = Poppins,
                fontSize = 10.sp,
                color = BalkanEstateGray
            )
        }
    }
}

// -----------------------------------------------------------------
// 4. Browse by City Section
// -----------------------------------------------------------------

@Composable
private fun BrowseByCitySection(
    onCityClick: (String) -> Unit
) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        SectionHeader(title = "Popular Cities")

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = popularCities) { city ->
                CityChip(
                    city = city,
                    onClick = { onCityClick(city) }
                )
            }
        }
    }
}

@Composable
private fun CityChip(
    city: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, BalkanEstatePrimaryBlue, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = LocationIcon,
                contentDescription = null,
                tint = BalkanEstatePrimaryBlue,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = city,
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                color = BalkanEstatePrimaryBlue
            )
        }
    }
}

// -----------------------------------------------------------------
// 5. Property Types Section
// -----------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PropertyTypesSection(
    onPropertyTypeClick: (String) -> Unit
) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        SectionHeader(title = "Property Types")

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            maxItemsInEachRow = 3
        ) {
            propertyTypes.forEach { type ->
                PropertyTypeCard(
                    type = type,
                    onClick = { onPropertyTypeClick(type.name) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PropertyTypeCard(
    type: PropertyType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = type.emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = type.name,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )
        }
    }
}

// -----------------------------------------------------------------
// 6. Top Agents Section
// -----------------------------------------------------------------

@Composable
private fun TopAgentsSection(
    agents: List<Agent>,
    isLoading: Boolean,
    onAgentClick: (String) -> Unit,
    onSeeAllClick: () -> Unit
) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        SectionHeader(
            title = "Top Agents",
            actionLabel = "See All",
            onActionClick = onSeeAllClick
        )

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
            }
        } else if (agents.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No agents available yet",
                    color = BalkanEstateGray,
                    fontFamily = Poppins,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = agents, key = { it.id }) { agent ->
                    AgentCard(
                        agent = agent,
                        onClick = { onAgentClick(agent.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AgentCard(
    agent: Agent,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(BalkanEstatePrimaryBlue.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = PersonIcon,
                    contentDescription = null,
                    tint = BalkanEstatePrimaryBlue,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = agent.name,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Color.DarkGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Rating
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = StarIcon,
                    contentDescription = null,
                    tint = BalkanEstateOrange,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = String.format("%.1f", agent.rating),
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${agent.listingsCount} listings",
                fontFamily = Poppins,
                fontSize = 11.sp,
                color = BalkanEstateGray
            )
        }
    }
}

// -----------------------------------------------------------------
// 7. Why Choose Us Section
// -----------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WhyChooseUsSection() {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        SectionHeader(title = "Why Choose Us")

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            maxItemsInEachRow = 2
        ) {
            whyChooseItems.forEach { item ->
                WhyChooseCard(
                    item = item,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun WhyChooseCard(
    item: WhyChooseItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
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
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item.emoji, fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.title,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = item.subtitle,
                fontFamily = Poppins,
                fontSize = 11.sp,
                color = BalkanEstateGray,
                textAlign = TextAlign.Center
            )
        }
    }
}

// -----------------------------------------------------------------
// Shared: Section Header
// -----------------------------------------------------------------

@Composable
private fun SectionHeader(
    title: String,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.DarkGray
        )
        if (actionLabel != null && onActionClick != null) {
            Text(
                text = actionLabel,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = BalkanEstatePrimaryBlue,
                modifier = Modifier.clickable { onActionClick() }
            )
        }
    }
}

// -----------------------------------------------------------------
// Previews
// -----------------------------------------------------------------

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    BalkanEstateTheme {
        HomeScreen(
            state = HomeState(
                featuredProperties = listOf(
                    BalkanEstateProperty(
                        id = "1",
                        title = "Modern Apartment in Tirana",
                        price = 120000.0,
                        currency = "EUR",
                        imageUrl = "",
                        bedrooms = 2,
                        bathrooms = 1,
                        squareFootage = 85,
                        address = "Blloku, Tirana",
                        city = "Tirana",
                        country = "Albania",
                        latitude = 41.32,
                        longitude = 19.82,
                        propertyType = "Apartment",
                        listingType = "Sale",
                        agentName = "Albi Realty",
                        isFeatured = true
                    ),
                    BalkanEstateProperty(
                        id = "2",
                        title = "Seaside Villa in Dubrovnik",
                        price = 450000.0,
                        currency = "EUR",
                        imageUrl = "",
                        bedrooms = 4,
                        bathrooms = 3,
                        squareFootage = 220,
                        address = "Old Town, Dubrovnik",
                        city = "Dubrovnik",
                        country = "Croatia",
                        latitude = 42.65,
                        longitude = 18.09,
                        propertyType = "Villa",
                        listingType = "Sale",
                        agentName = "Adriatic Homes",
                        isFeatured = true
                    )
                ),
                topAgents = listOf(
                    Agent(
                        id = "a1",
                        name = "Marko Petrovic",
                        rating = 4.9f,
                        listingsCount = 45,
                        isVerified = true
                    ),
                    Agent(
                        id = "a2",
                        name = "Elena Dimitrova",
                        rating = 4.8f,
                        listingsCount = 38,
                        isVerified = true
                    ),
                    Agent(
                        id = "a3",
                        name = "Arta Hoxha",
                        rating = 4.7f,
                        listingsCount = 32,
                        isVerified = false
                    )
                ),
                isLoadingProperties = false,
                isLoadingAgents = false
            ),
            onAction = {}
        )
    }
}

@Preview(showBackground = true, name = "Home Screen Loading")
@Composable
private fun HomeScreenLoadingPreview() {
    BalkanEstateTheme {
        HomeScreen(
            state = HomeState(
                isLoadingProperties = true,
                isLoadingAgents = true
            ),
            onAction = {}
        )
    }
}

@Preview(showBackground = true, name = "Hero Section")
@Composable
private fun HeroSectionPreview() {
    BalkanEstateTheme {
        HeroSection(
            searchQuery = "",
            onSearchQueryChanged = {},
            onSearchClick = {},
            onBrowseClick = {}
        )
    }
}
