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
fun PrivacyPolicyScreenRoot(
    onNavigateBack: () -> Unit
) {
    PrivacyPolicyScreen(
        onBackClick = onNavigateBack
    )
}

@Composable
private fun PrivacyPolicyScreen(
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
                text = "Privacy Policy",
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
                        text = "Balkan Estate AI (\"we\", \"our\", or \"us\") is committed to protecting your privacy. This Privacy Policy explains how we collect, use, disclose, and safeguard your information when you use our mobile application and services.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Information We Collect
            PolicySection(
                title = "1. Information We Collect",
                content = "We collect information you provide directly to us, including:\n\n" +
                        "\u2022 Personal Information: Name, email address, phone number, and location when you create an account.\n\n" +
                        "\u2022 Property Data: Information about properties you list, search for, or save, including preferences and search history.\n\n" +
                        "\u2022 Usage Data: Information about how you interact with our app, including pages viewed, features used, and time spent.\n\n" +
                        "\u2022 Device Information: Device type, operating system, unique device identifiers, and mobile network information.\n\n" +
                        "\u2022 Location Data: With your permission, we collect precise location data to provide location-based property search results."
            )

            Spacer(modifier = Modifier.height(12.dp))

            // How We Use Your Information
            PolicySection(
                title = "2. How We Use Your Information",
                content = "We use the information we collect to:\n\n" +
                        "\u2022 Provide, maintain, and improve our services and features.\n\n" +
                        "\u2022 Process property listings and facilitate communication between buyers and agents.\n\n" +
                        "\u2022 Personalize your experience with AI-powered property recommendations.\n\n" +
                        "\u2022 Send you notifications about new listings, price changes, and relevant updates.\n\n" +
                        "\u2022 Analyze usage patterns to improve our platform and develop new features.\n\n" +
                        "\u2022 Detect, prevent, and address fraud, abuse, and technical issues."
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Information Sharing
            PolicySection(
                title = "3. Information Sharing",
                content = "We may share your information in the following circumstances:\n\n" +
                        "\u2022 With Agents: When you express interest in a property, your contact information may be shared with the listing agent.\n\n" +
                        "\u2022 Service Providers: We work with third-party companies that help us operate our platform, such as cloud hosting, analytics, and payment processing.\n\n" +
                        "\u2022 Legal Requirements: We may disclose your information if required by law, regulation, or legal process.\n\n" +
                        "\u2022 Business Transfers: In connection with a merger, acquisition, or sale of assets, your information may be transferred.\n\n" +
                        "We do not sell your personal information to third parties for marketing purposes."
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Data Security
            PolicySection(
                title = "4. Data Security",
                content = "We implement appropriate technical and organizational security measures to protect your personal information against unauthorized access, alteration, disclosure, or destruction. These measures include:\n\n" +
                        "\u2022 Encryption of data in transit and at rest using industry-standard protocols.\n\n" +
                        "\u2022 Regular security assessments and penetration testing.\n\n" +
                        "\u2022 Access controls limiting employee access to personal data on a need-to-know basis.\n\n" +
                        "\u2022 Secure data centers with physical security measures.\n\n" +
                        "While we strive to protect your information, no method of electronic transmission or storage is 100% secure."
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Your Rights
            PolicySection(
                title = "5. Your Rights",
                content = "Depending on your location, you may have certain rights regarding your personal information:\n\n" +
                        "\u2022 Access: You can request a copy of the personal data we hold about you.\n\n" +
                        "\u2022 Correction: You can request correction of inaccurate or incomplete data.\n\n" +
                        "\u2022 Deletion: You can request deletion of your personal data, subject to certain exceptions.\n\n" +
                        "\u2022 Data Portability: You can request a machine-readable copy of your data.\n\n" +
                        "\u2022 Opt-Out: You can opt out of marketing communications at any time through your notification settings.\n\n" +
                        "\u2022 Withdraw Consent: Where processing is based on consent, you may withdraw it at any time."
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Contact Us
            PolicySection(
                title = "6. Contact Us",
                content = "If you have any questions or concerns about this Privacy Policy or our data practices, please contact us:\n\n" +
                        "\u2022 Email: privacy@balkanestateai.com\n\n" +
                        "\u2022 Phone: +355 69 123 4567\n\n" +
                        "\u2022 Address: Rruga Ismail Qemali, Nr. 27, Tirana, Albania\n\n" +
                        "We will respond to your inquiry within 30 days."
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PolicySection(
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
private fun PrivacyPolicyScreenPreview() {
    BalkanEstateTheme {
        PrivacyPolicyScreen(
            onBackClick = {}
        )
    }
}
