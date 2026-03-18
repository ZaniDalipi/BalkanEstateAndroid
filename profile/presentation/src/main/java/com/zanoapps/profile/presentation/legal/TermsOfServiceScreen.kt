package com.zanoapps.profile.presentation.legal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BackIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme

@Composable
fun TermsOfServiceScreenRoot(
    onNavigateBack: () -> Unit
) {
    TermsOfServiceScreen(
        onBackClick = onNavigateBack
    )
}

@Composable
private fun TermsOfServiceScreen(
    onBackClick: () -> Unit
) {
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
                text = "Terms of Service",
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
            // Last Updated
            Text(
                text = "Last updated: January 15, 2026",
                style = MaterialTheme.typography.bodySmall,
                color = BalkanEstateGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Please read these Terms of Service (\"Terms\") carefully before using the Balkan Estate AI mobile application and services operated by Balkan Estate AI (\"we\", \"our\", or \"us\").",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Acceptance of Terms
            TermsSection(
                title = "1. Acceptance of Terms",
                content = "By accessing or using our application, you agree to be bound by these Terms. If you do not agree to these Terms, you may not access or use the application.\n\n" +
                        "We reserve the right to modify these Terms at any time. We will notify you of any changes by posting the updated Terms on the application with a new effective date. Your continued use of the application after such changes constitutes your acceptance of the new Terms.\n\n" +
                        "You must be at least 18 years old to use our services. By using the application, you represent and warrant that you meet this age requirement."
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Use of Service
            TermsSection(
                title = "2. Use of Service",
                content = "Our application provides a platform for property listing, searching, and connecting buyers with agents across the Balkans region. You agree to use the service only for lawful purposes and in accordance with these Terms.\n\n" +
                        "You agree not to:\n\n" +
                        "\u2022 Use the service for any illegal or unauthorized purpose.\n\n" +
                        "\u2022 Post false, misleading, or fraudulent property listings.\n\n" +
                        "\u2022 Interfere with or disrupt the service or servers.\n\n" +
                        "\u2022 Attempt to gain unauthorized access to any part of the service.\n\n" +
                        "\u2022 Use automated systems to scrape or extract data from the service.\n\n" +
                        "\u2022 Harass, abuse, or harm other users or agents."
            )

            Spacer(modifier = Modifier.height(12.dp))

            // User Accounts
            TermsSection(
                title = "3. User Accounts",
                content = "To access certain features, you must create an account. You are responsible for:\n\n" +
                        "\u2022 Maintaining the confidentiality of your account credentials.\n\n" +
                        "\u2022 All activities that occur under your account.\n\n" +
                        "\u2022 Providing accurate and complete information during registration.\n\n" +
                        "\u2022 Updating your information to keep it current and accurate.\n\n" +
                        "We reserve the right to suspend or terminate accounts that violate these Terms or are inactive for an extended period. You may delete your account at any time through the application settings."
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Property Listings
            TermsSection(
                title = "4. Property Listings",
                content = "If you list a property on our platform, you represent and warrant that:\n\n" +
                        "\u2022 You have the legal right to list the property for sale or rent.\n\n" +
                        "\u2022 All information provided about the property is accurate and complete.\n\n" +
                        "\u2022 All photos and media are authentic and accurately represent the property.\n\n" +
                        "\u2022 The listing complies with all applicable local laws and regulations.\n\n" +
                        "We reserve the right to remove any listing that violates these Terms or that we determine, in our sole discretion, to be inappropriate, misleading, or fraudulent. Balkan Estate AI does not guarantee the accuracy of any listing and is not a party to any transaction between users."
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Intellectual Property
            TermsSection(
                title = "5. Intellectual Property",
                content = "The application and its original content, features, and functionality are owned by Balkan Estate AI and are protected by international copyright, trademark, patent, trade secret, and other intellectual property laws.\n\n" +
                        "Our AI-powered search technology, algorithms, and proprietary data analysis tools are the exclusive property of Balkan Estate AI.\n\n" +
                        "You retain ownership of content you submit to the platform, but grant us a non-exclusive, worldwide, royalty-free license to use, display, and distribute such content in connection with our services."
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Limitation of Liability
            TermsSection(
                title = "6. Limitation of Liability",
                content = "To the maximum extent permitted by applicable law, Balkan Estate AI shall not be liable for any indirect, incidental, special, consequential, or punitive damages, including but not limited to:\n\n" +
                        "\u2022 Loss of profits, data, or business opportunities.\n\n" +
                        "\u2022 Property valuation inaccuracies.\n\n" +
                        "\u2022 Disputes between buyers, sellers, or agents.\n\n" +
                        "\u2022 Service interruptions or data loss.\n\n" +
                        "The service is provided \"as is\" and \"as available\" without warranties of any kind, either express or implied. We do not warrant that the service will be uninterrupted, secure, or error-free."
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Governing Law
            TermsSection(
                title = "7. Governing Law",
                content = "These Terms shall be governed by and construed in accordance with the laws of the Republic of Albania, without regard to its conflict of law provisions.\n\n" +
                        "Any disputes arising from or relating to these Terms or the use of the service shall be resolved through binding arbitration in Tirana, Albania, in accordance with the rules of the Albanian Chamber of Commerce.\n\n" +
                        "If any provision of these Terms is found to be unenforceable, the remaining provisions shall continue in full force and effect."
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Contact Information
            TermsSection(
                title = "8. Contact Information",
                content = "If you have any questions about these Terms of Service, please contact us:\n\n" +
                        "\u2022 Email: legal@balkanestateai.com\n\n" +
                        "\u2022 Phone: +355 69 123 4567\n\n" +
                        "\u2022 Address: Rruga Ismail Qemali, Nr. 27, Tirana, Albania"
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TermsSection(
    title: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BalkanEstatePrimaryBlue
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                lineHeight = 22.sp
            )
        }
    }
}

@Preview
@Composable
private fun TermsOfServiceScreenPreview() {
    BalkanEstateTheme {
        TermsOfServiceScreen(
            onBackClick = {}
        )
    }
}
