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
import androidx.core.net.toUri

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // --- TOP HEADER ---
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = OsakaRed,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "About Osaka Group",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
            }

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
                    borderColor = borderColor,
                    textColor = textColor
                ) {
                    Text(
                        text = "Since 1994, Osaka Group has been a leader in high-end manufacturing, with a proven legacy of producing countless home appliances and selling over 2 million televisions.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
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
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        number = "32+",
                        label = "Years of Innovation",
                        cardBg = cardBg,
                        borderColor = borderColor,
                        mutedTextColor = mutedTextColor
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        number = "2M+",
                        label = "Stories of Trust",
                        cardBg = cardBg,
                        borderColor = borderColor,
                        mutedTextColor = mutedTextColor
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
                    modifier = Modifier.padding(vertical = 2.dp)
                )

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(cardBg)
                        .border(1.dp, borderColor, RoundedCornerShape(14.dp))
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
                            textColor = textColor,
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
                    modifier = Modifier.padding(vertical = 2.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    LocationCard(
                        tag = "Corporate Office",
                        address = "মোহাম্মদপুর, কাদেরাবাদ হাউসিং, রোড ৫, ব্লক বি, বাসা ৪, গ্রাউন্ড ফ্লোর ।",
                        phone = "01886469096",
                        cardBg = cardBg,
                        borderColor = borderColor,
                        textColor = textColor,
                        onCall = { dialPhone(context, "01886469096") }
                    )

                    LocationCard(
                        tag = "Wholesale Center",
                        address = "গুলিস্তান, কাপ্তান বাজার কম্পলেক্স -ভবন ২, ২য় তলা, দোকান নং- ১০৫ (105) & ১০৬ (106), নওবাবপুর রোড, ঢাকা।",
                        phone = "01934009834",
                        cardBg = cardBg,
                        borderColor = borderColor,
                        textColor = textColor,
                        onCall = { dialPhone(context, "01934009834") }
                    )

                    LocationCard(
                        tag = "Sales Center",
                        address = "এলিফ্যান্ট রোড, আইসিটি ভবন (সুভাসতু আর্কেড), লেভেল ৩, দোকান নং: ৩০৮ (308)।",
                        phone = "01401111245",
                        cardBg = cardBg,
                        borderColor = borderColor,
                        textColor = textColor,
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
            .clip(RoundedCornerShape(16.dp))
            .background(OsakaRedLightBg)
            .border(1.dp, OsakaRedLight, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Surface(
                color = OsakaRed,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "EST. 1994",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "OSAKA GROUP",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = OsakaRed
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "32 Years of Innovation | Two Million Stories of Trust",
                fontSize = 12.sp,
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
    textColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(cardBg)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = OsakaRed,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        content()
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    number: String,
    label: String,
    cardBg: Color,
    borderColor: Color,
    mutedTextColor: Color
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(cardBg)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = number,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = OsakaRed
        )
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = mutedTextColor
        )
    }
}

@Composable
private fun NavigationRow(
    title: String,
    icon: ImageVector,
    textColor: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = OsakaRed,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = textColor.copy(alpha = 0.4f),
                modifier = Modifier.size(18.dp)
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
    textColor: Color,
    onCall: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(cardBg)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Surface(
            color = OsakaRedLight,
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = tag,
                color = OsakaRedHover,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = address,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onCall,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(WhatsappGreen)
                    .size(34.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Call",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

private fun dialPhone(context: android.content.Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = "tel:$phoneNumber".toUri()
    }
    context.startActivity(intent)
}