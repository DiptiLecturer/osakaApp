package org.freedu.osakatelevison.ui.presentation


import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.freedu.osakatelevison.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(
    onNavigate: (String) -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) DarkBackground else LightBackground
    val cardBg = if (isDark) DarkSecondary else LightSecondary
    val textColor = if (isDark) DarkForeground else LightForeground
    val mutedTextColor = if (isDark) DarkMutedForeground else LightMutedForeground
    val borderColor = if (isDark) DarkBorder else LightBorder

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About Osaka Group", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = bgColor,
                    titleContentColor = textColor
                )
            )
        },
        containerColor = bgColor
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // --- HERO BANNER ---
            item {
                HeroHeaderSection()
            }

            // --- OUR STORY ---
            item {
                SectionCard(
                    title = "Our Story",
                    icon = Icons.Outlined.AutoAwesome,
                    cardBg = cardBg,
                    borderColor = borderColor
                ) {
                    Text(
                        text = "Since 1994, Osaka Group has been a leader in high-end manufacturing, with a proven legacy of producing countless home appliances and selling over 2 million televisions.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Redefining the standards of home electronics in Bangladesh. We bring you premium technology designed for durability, efficiency, and the ultimate user experience.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = mutedTextColor,
                        lineHeight = 22.sp
                    )
                }
            }

            // --- STATS BADGES ---
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        number = "32+",
                        label = "Years of Innovation",
                        cardBg = cardBg,
                        borderColor = borderColor
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        number = "2M+",
                        label = "Stories of Trust",
                        cardBg = cardBg,
                        borderColor = borderColor
                    )
                }
            }

            // --- NAVIGATION LINKS ---
            item {
                Text(
                    text = "Quick Navigation",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(cardBg)
                        .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                ) {
                    val navItems = listOf(
                        Triple("Digital Showroom", Icons.Outlined.Storefront, "digital_showroom"),
                        Triple("Our Legacy", Icons.Outlined.History, "our_legacy"),
                        Triple("All Products", Icons.Outlined.Tv, "all_products"),
                        Triple("Visual Gallery", Icons.Outlined.Collections, "visual_gallery"),
                        Triple("Showrooms", Icons.Outlined.Place, "showrooms"),
                        Triple("Get in Touch", Icons.Outlined.SupportAgent, "get_in_touch")
                    )

                    navItems.forEachIndexed { index, item ->
                        NavigationRow(
                            title = item.first,
                            icon = item.second,
                            onClick = { onNavigate(item.third) }
                        )
                        if (index < navItems.lastIndex) {
                            HorizontalDivider(color = borderColor)
                        }
                    }
                }
            }

            // --- SHOWROOMS & OFFICES ---
            item {
                Text(
                    text = "Our Locations",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    LocationCard(
                        tag = "Corporate Office",
                        address = "মোহাম্মদপুর, কাদেরাবাদ হাউসিং, রোড ৫, ব্লক বি, বাসা ৪, গ্রাউন্ড ফ্লোর ।",
                        phone = "01886469096",
                        cardBg = cardBg,
                        borderColor = borderColor,
                        onCall = { dialPhone(context, "01886469096") }
                    )

                    LocationCard(
                        tag = "Wholesale Center",
                        address = "গুলিস্তান, কাপ্তান বাজার কম্পলেক্স -ভবন ২, ২য় তলা, দোকান নং- ১০৫ (105) & ১০৬ (106), নওবাবপুর রোড, ঢাকা।",
                        phone = "01934009834",
                        cardBg = cardBg,
                        borderColor = borderColor,
                        onCall = { dialPhone(context, "01934009834") }
                    )

                    LocationCard(
                        tag = "Sales Center",
                        address = "এলিফ্যান্ট রোড, আইসিটি ভবন (সুভাসতু আর্কেড), লেভেল ৩, দোকান নং: ৩০৮ (308)।",
                        phone = "01401111245",
                        cardBg = cardBg,
                        borderColor = borderColor,
                        onCall = { dialPhone(context, "01401111245") }
                    )
                }
            }
        }
    }
}

// --- HELPER COMPOSABLES ---

@Composable
private fun HeroHeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(OsakaRedLightBg)
            .border(1.dp, OsakaRedLight, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column {
            Surface(
                color = OsakaRed,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "EST. 1994",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "OSAKA GROUP",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = OsakaRed
            )
            Text(
                text = "32 Years of Innovation | Two Million Stories of Trust",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = LightForeground.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    icon: ImageVector,
    cardBg: Color,
    borderColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = OsakaRed
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    number: String,
    label: String,
    cardBg: Color,
    borderColor: Color
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = number,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = OsakaRed
        )
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun NavigationRow(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = OsakaRed,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun LocationCard(
    tag: String,
    address: String,
    phone: String,
    cardBg: Color,
    borderColor: Color,
    onCall: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Surface(
            color = OsakaRedLight,
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = tag,
                color = OsakaRedHover,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = address,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onCall,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(WhatsappGreen)
                    .size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Call",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

private fun dialPhone(context: android.content.Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    context.startActivity(intent)
}