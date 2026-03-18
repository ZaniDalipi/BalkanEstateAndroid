package com.zanoapps.ads.presentation.ads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.ads.domain.model.Ad
import com.zanoapps.ads.domain.model.AdPlacement
import com.zanoapps.ads.domain.model.AdType
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.presentation.ui.ObserveAsEvents
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdBannerRoot(
    placement: AdPlacement,
    viewModel: AdViewModel = koinViewModel(),
    onAdUrlOpen: (String) -> Unit = {}
) {
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is AdEvent.OpenAdUrl -> onAdUrlOpen(event.url)
            is AdEvent.Error -> Unit
        }
    }

    LaunchedEffect(placement) {
        viewModel.onAction(AdAction.OnLoadAds(placement))
    }

    AdBannerScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
fun AdBannerScreen(
    state: AdState,
    onAction: (AdAction) -> Unit
) {
    if (state.ads.isEmpty()) return

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        state.ads.forEach { ad ->
            when (ad.type) {
                AdType.BANNER -> AdBannerCard(
                    ad = ad,
                    onAction = onAction
                )
                AdType.FEATURED_LISTING -> FeaturedListingCard(
                    ad = ad,
                    onAction = onAction
                )
                AdType.SPONSORED_AGENT -> SponsoredAgentCard(
                    ad = ad,
                    onAction = onAction
                )
                AdType.INTERSTITIAL -> AdBannerCard(
                    ad = ad,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
fun AdBannerCard(
    ad: Ad,
    onAction: (AdAction) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(ad.id) {
        onAction(AdAction.OnAdImpression(ad.id))
    }

    Card(
        onClick = { onAction(AdAction.OnAdClick(ad.id)) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ad.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Sponsored badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(BalkanEstateOrange.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Sponsored",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BalkanEstateOrange
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = ad.description,
                style = MaterialTheme.typography.bodyMedium,
                color = BalkanEstateGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onAction(AdAction.OnAdClick(ad.id)) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BalkanEstatePrimaryBlue
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Learn More",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun FeaturedListingCard(
    ad: Ad,
    onAction: (AdAction) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(ad.id) {
        onAction(AdAction.OnAdImpression(ad.id))
    }

    Card(
        onClick = { onAction(AdAction.OnAdClick(ad.id)) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp,
            pressedElevation = 6.dp
        )
    ) {
        Column {
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Featured Property",
                    style = MaterialTheme.typography.bodyLarge,
                    color = BalkanEstatePrimaryBlue,
                    fontWeight = FontWeight.Medium
                )

                // Sponsored badge overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(BalkanEstateOrange)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Sponsored",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = ad.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = ad.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = BalkanEstateGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { onAction(AdAction.OnAdClick(ad.id)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BalkanEstateGreen
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "View Property",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun SponsoredAgentCard(
    ad: Ad,
    onAction: (AdAction) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(ad.id) {
        onAction(AdAction.OnAdImpression(ad.id))
    }

    Card(
        onClick = { onAction(AdAction.OnAdClick(ad.id)) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar placeholder
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(BalkanEstatePrimaryBlue.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ad.title
                        .replace("Top Agent: ", "")
                        .split(" ")
                        .take(2)
                        .mapNotNull { it.firstOrNull()?.uppercase() }
                        .joinToString(""),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BalkanEstatePrimaryBlue
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ad.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(BalkanEstateOrange.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Sponsored",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BalkanEstateOrange
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = ad.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = BalkanEstateGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onAction(AdAction.OnAdClick(ad.id)) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BalkanEstatePrimaryBlue
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Contact Agent",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AdBannerScreenPreview() {
    BalkanEstateTheme {
        AdBannerScreen(
            state = AdState(
                ads = listOf(
                    Ad(
                        id = "1",
                        title = "Premium Property in Tirana",
                        description = "Beautiful apartment with stunning views in the heart of the city",
                        type = AdType.BANNER
                    ),
                    Ad(
                        id = "2",
                        title = "Luxury Villa in Saranda",
                        description = "Seafront villa with private pool and garden",
                        type = AdType.FEATURED_LISTING
                    ),
                    Ad(
                        id = "3",
                        title = "Top Agent: Maria Johnson",
                        description = "15 years experience in Balkan real estate market",
                        type = AdType.SPONSORED_AGENT
                    )
                )
            ),
            onAction = {}
        )
    }
}
