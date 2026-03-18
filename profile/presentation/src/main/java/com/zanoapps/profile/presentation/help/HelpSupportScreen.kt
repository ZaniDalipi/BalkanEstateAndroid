package com.zanoapps.profile.presentation.help

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BackIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.EmailIcon
import com.zanoapps.core.presentation.designsystem.KeyboardArrowDownIcon
import com.zanoapps.core.presentation.designsystem.KeyboardArrowUpIcon

@Composable
fun HelpSupportScreenRoot(
    onNavigateBack: () -> Unit
) {
    HelpSupportScreen(
        onBackClick = onNavigateBack
    )
}

@Composable
private fun HelpSupportScreen(
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val faqItems = remember {
        listOf(
            FaqItem(
                question = "How do I list my property?",
                answer = "To list your property, go to My Listings from your profile and tap the \"Add New Listing\" button. Fill in the property details including title, description, price, location, and upload photos. Your listing will be reviewed and published within 24 hours."
            ),
            FaqItem(
                question = "How does the AI search work?",
                answer = "Our AI-powered search understands natural language queries. Simply describe what you're looking for, such as \"3 bedroom apartment near the beach in Tirana under 100k\", and the AI will find the best matching properties. The more specific you are, the better the results."
            ),
            FaqItem(
                question = "What countries are supported?",
                answer = "Balkan Estate AI currently supports property listings across the Balkans region, including Albania, Kosovo, North Macedonia, Montenegro, Serbia, Bosnia and Herzegovina, and Croatia. We are continuously expanding to cover more countries."
            ),
            FaqItem(
                question = "How do I contact an agent?",
                answer = "You can contact an agent directly from any property listing page. Tap the \"Contact Agent\" button to send a message, call, or email the listing agent. You can also browse agents from the Agents section in the app."
            ),
            FaqItem(
                question = "How do I reset my password?",
                answer = "To reset your password, go to the login screen and tap \"Forgot Password\". Enter your registered email address and we'll send you a password reset link. Follow the link to create a new password. If you don't receive the email, check your spam folder."
            ),
            FaqItem(
                question = "What are the subscription plans?",
                answer = "We offer three plans: Free (basic search and limited listings), Premium (unlimited searches, advanced filters, market insights), and Enterprise (all Premium features plus API access, team management, and dedicated support). Visit the Subscription section in your profile to learn more."
            )
        )
    }

    val filteredFaqs = remember(searchQuery) {
        if (searchQuery.isBlank()) faqItems
        else faqItems.filter {
            it.question.contains(searchQuery, ignoreCase = true) ||
                    it.answer.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(BackIcon, "Back", Modifier.size(20.dp))
            }
            Text(
                text = "Help & Support",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search FAQ...", color = BalkanEstateGray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BalkanEstatePrimaryBlue,
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // FAQ Section
            Text(
                text = "Frequently Asked Questions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    filteredFaqs.forEachIndexed { index, faq ->
                        FaqExpandableItem(faq = faq)
                        if (index < filteredFaqs.lastIndex) {
                            HorizontalDivider(color = Color(0xFFF1F5F9))
                        }
                    }
                    if (filteredFaqs.isEmpty()) {
                        Text(
                            text = "No results found for \"$searchQuery\"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = BalkanEstateGray,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Contact Us Section
            Text(
                text = "Contact Us",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = EmailIcon,
                            contentDescription = null,
                            tint = BalkanEstatePrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Email",
                                style = MaterialTheme.typography.bodySmall,
                                color = BalkanEstateGray
                            )
                            Text(
                                text = "support@balkanestateai.com",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = BalkanEstatePrimaryBlue
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = EmailIcon,
                            contentDescription = null,
                            tint = BalkanEstatePrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Phone",
                                style = MaterialTheme.typography.bodySmall,
                                color = BalkanEstateGray
                            )
                            Text(
                                text = "+355 69 123 4567",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = BalkanEstatePrimaryBlue
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Report a Problem Button
            Button(
                onClick = { /* Report a problem action */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BalkanEstateOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Report a Problem",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private data class FaqItem(
    val question: String,
    val answer: String
)

@Composable
private fun FaqExpandableItem(faq: FaqItem) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        onClick = { expanded = !expanded },
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = faq.question,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) KeyboardArrowUpIcon else KeyboardArrowDownIcon,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = BalkanEstateGray,
                    modifier = Modifier.size(20.dp)
                )
            }
            AnimatedVisibility(visible = expanded) {
                Text(
                    text = faq.answer,
                    style = MaterialTheme.typography.bodySmall,
                    color = BalkanEstateGray,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun HelpSupportScreenPreview() {
    BalkanEstateTheme {
        HelpSupportScreen(
            onBackClick = {}
        )
    }
}
