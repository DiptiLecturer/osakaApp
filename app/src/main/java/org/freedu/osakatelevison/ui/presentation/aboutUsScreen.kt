package org.freedu.osakatelevison.ui.presentation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import org.freedu.osakatelevison.ui.theme.*

private data class NavGridItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

private data class LocationItem(
    val tag: String,
    val address: String,
    val phone: String
)

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

    val navItems = listOf(
        NavGridItem("Showroom", Icons.Outlined.Storefront, "digital_showroom"),
        NavGridItem("Our Legacy", Icons.Outlined.History, "our_legacy"),
        NavGridItem("Products", Icons.Outlined.Tv, "all_products"),
        NavGridItem("Gallery", Icons.Outlined.Collections, "visual_gallery"),
        NavGridItem("Locations", Icons.Outlined.Place, "showrooms"),
        NavGridItem("Contact Us", Icons.Outlined.SupportAgent, "get_in_touch")
    )

    val locations = listOf(
        LocationItem(
            tag = "Corporate Office",
            address = "মোহাম্মদপুর, কাদেরাবাদ হাউসিং, রোড ৫, ব্লক বি, বাসা ৪, গ্রাউন্ড ফ্লোর ।",
            phone = "01886469096"
        ),
        LocationItem(
            tag = "Wholesale Center",
            address = "গুলিস্তান, কাপ্তান বাজার কম্পলেক্স - ভবন ২, ২য় তলা, দোকান ১০৫-১০৬, ঢাকা।",
            phone = "01934009834"
        ),
        LocationItem(
            tag = "Sales Center",
            address = "এলিফ্যান্ট রোড, আইসিটি ভবন (সুভাসতু আর্কেড), লেভেল ৩, দোকান ৩০৮।",
            phone = "01401111245"
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // --- TOP HEADER (Full Width) ---
            item(span = { GridItemSpan(2) }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = OsakaRed,
                        modifier = Modifier.size(24.dp)
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

            // --- HERO BANNER (Full Width) ---
            item(span = { GridItemSpan(2) }) {
                HeroHeaderSection()
            }

            // --- OUR STORY (Full Width) ---
            item(span = { GridItemSpan(2) }) {
                SectionCard(
                    title = "Our Story",
                    icon = Icons.Outlined.AutoAwesome,
                    cardBg = cardBg,
                    borderColor = borderColor,
                    textColor = textColor
                ) {
                    Text(
                        text = "Since 1994, Osaka Group has been a leader in high-end manufacturing with over 2 million televisions sold across Bangladesh.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "We engineer premium, durable electronics designed specifically to elevate the everyday living experience.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = mutedTextColor,
                        lineHeight = 20.sp
                    )
                }
            }

            // --- STATS BADGES (2 Columns) ---
            item {
                GridStatTile(
                    number = "32+",
                    label = "Years Active",
                    cardBg = cardBg,
                    borderColor = borderColor,
                    mutedTextColor = mutedTextColor
                )
            }
            item {
                GridStatTile(
                    number = "2M+",
                    label = "TVs Sold",
                    cardBg = cardBg,
                    borderColor = borderColor,
                    mutedTextColor = mutedTextColor
                )
            }

            // --- QUICK NAVIGATION HEADER (Full Width) ---
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = "Quick Navigation",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // --- QUICK NAVIGATION TILES (2 Columns) ---
            items(navItems) { nav ->
                NavigationGridTile(
                    item = nav,
                    cardBg = cardBg,
                    borderColor = borderColor,
                    textColor = textColor,
                    onClick = { onNavigate(nav.route) }
                )
            }

            // --- SHOWROOMS & OFFICES HEADER (Full Width) ---
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = "Our Locations",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // --- LOCATION CARDS (Full Width) ---
            items(locations, span = { GridItemSpan(2) }) { location ->
                LocationCard(
                    location = location,
                    cardBg = cardBg,
                    borderColor = borderColor,
                    textColor = textColor,
                    onCall = { dialPhone(context, location.phone) }
                )
            }
        }
    }
}

// --- SUB-COMPONENTS ---

@Composable
private fun HeroHeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(OsakaRedLightBg)
            .border(1.dp, OsakaRedLight, RoundedCornerShape(16.dp))
            .padding(18.dp)
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
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = OsakaRed
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "32 Years of Innovation | Two Million Stories of Trust",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = LightForeground.copy(alpha = 0.85f)
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
            .padding(16.dp)
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
private fun GridStatTile(
    number: String,
    label: String,
    cardBg: Color,
    borderColor: Color,
    mutedTextColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(cardBg)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = number,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = OsakaRed
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = mutedTextColor
        )
    }
}

@Composable
private fun NavigationGridTile(
    item: NavGridItem,
    cardBg: Color,
    borderColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = cardBg,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(OsakaRedLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = OsakaRed,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = textColor.copy(alpha = 0.3f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun LocationCard(
    location: LocationItem,
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = OsakaRedLight,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = location.tag,
                    color = OsakaRedHover,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            IconButton(
                onClick = onCall,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(WhatsappGreen)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Call ${location.tag}",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = location.address,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            lineHeight = 20.sp
        )
    }
}

private fun dialPhone(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = "tel:$phoneNumber".toUri()
    }
    context.startActivity(intent)
}