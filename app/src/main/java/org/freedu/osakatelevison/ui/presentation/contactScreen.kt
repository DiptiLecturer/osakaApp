package org.freedu.osakatelevison.ui.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.freedu.osakatelevison.ui.theme.*
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen() {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()

    // Dynamic color picks based on palette
    val bgColor = if (isDark) DarkBackground else LightBackground
    val textColor = if (isDark) DarkForeground else LightForeground
    val mutedTextColor = if (isDark) DarkMutedForeground else LightMutedForeground
    val cardBgColor = if (isDark) DarkSecondary else LightSecondary
    val borderColor = if (isDark) DarkBorder else LightBorder

    // Helper action handlers
    fun makePhoneCall(rawPhone: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$rawPhone".toUri()
        }
        context.startActivity(intent)
    }

    fun openWhatsApp(rawPhone: String) {
        val cleanNumber = rawPhone.replace("[^0-9]".toRegex(), "")
        val formattedNumber = if (cleanNumber.startsWith("0")) "+88$cleanNumber" else cleanNumber
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = "https://api.whatsapp.com/send?phone=$formattedNumber".toUri()
        }
        context.startActivity(intent)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Screen Title Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Call,
                    contentDescription = null,
                    tint = OsakaRed,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Contact Us",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }

            // Header Banner
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = OsakaRedLightBg
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Get In Touch",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OsakaRed
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Official OSAKA Point",
                        fontSize = 13.sp,
                        color = OsakaRedHover
                    )
                }
            }

            // Primary Contact Action Card
            ContactActionCard(
                title = "Phone",
                subtitle = "01886-469096",
                icon = Icons.Outlined.Call,
                iconBgColor = OsakaRedLight,
                iconTintColor = OsakaRed,
                cardBgColor = cardBgColor,
                borderColor = borderColor,
                textColor = textColor,
                mutedTextColor = mutedTextColor,
                onCallClick = { makePhoneCall("01886469096") },
                onWhatsappClick = { openWhatsApp("01886469096") }
            )

            Text(
                text = "Visit Our Showroom",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Corporate Office Card
            LocationCard(
                title = "Corporate Office",
                address = "মোহাম্মদপুর, কাদেরাবাদ হাউজিং, রোড ৫, ব্লক বি, বাসা ৪, গ্রাউন্ড ফ্লোর ।",
                phone = "01886469096",
                icon = Icons.Outlined.Business,
                cardBgColor = cardBgColor,
                borderColor = borderColor,
                textColor = textColor,
                mutedTextColor = mutedTextColor,
                onCallClick = { makePhoneCall("01886469096") },
                onWhatsappClick = { openWhatsApp("01886469096") }
            )

            // Wholesale Center Card
            LocationCard(
                title = "Wholesale Center",
                address = "গুলিস্তান, কাপ্তান বাজার কম্পলেক্স -ভবন ২, ২য় তলা,\nদোকান নং- ১০৫ (105) & ১০৬ (106), নবাবপুর রোড, ঢাকা।",
                phone = "01934009834",
                icon = Icons.Outlined.Storefront,
                cardBgColor = cardBgColor,
                borderColor = borderColor,
                textColor = textColor,
                mutedTextColor = mutedTextColor,
                onCallClick = { makePhoneCall("01934009834") },
                onWhatsappClick = { openWhatsApp("01934009834") }
            )

            // Sales Center Card
            LocationCard(
                title = "Sales Center",
                address = "এলিফ্যান্ট রোড, আইসিটি ভবন (সুভাসতু আর্কেড),\nলেভেল ৩, দোকান নং: ৩০৮ (308)।",
                phone = "01401111245",
                icon = Icons.Outlined.LocationOn,
                cardBgColor = cardBgColor,
                borderColor = borderColor,
                textColor = textColor,
                mutedTextColor = mutedTextColor,
                onCallClick = { makePhoneCall("01401111245") },
                onWhatsappClick = { openWhatsApp("01401111245") }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ContactActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBgColor: Color,
    iconTintColor: Color,
    cardBgColor: Color,
    borderColor: Color,
    textColor: Color,
    mutedTextColor: Color,
    onCallClick: () -> Unit,
    onWhatsappClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTintColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 11.sp, color = mutedTextColor)
                Text(
                    text = subtitle,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                IconButton(onClick = onCallClick) {
                    Icon(
                        imageVector = Icons.Outlined.Call,
                        contentDescription = "Call",
                        tint = OsakaRed,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Button(
                    onClick = onWhatsappClick,
                    colors = ButtonDefaults.buttonColors(containerColor = WhatsappGreen),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(text = "WhatsApp", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun LocationCard(
    title: String,
    address: String,
    phone: String,
    icon: ImageVector,
    cardBgColor: Color,
    borderColor: Color,
    textColor: Color,
    mutedTextColor: Color,
    onCallClick: () -> Unit,
    onWhatsappClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = OsakaRed,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = address,
                fontSize = 14.sp,
                color = mutedTextColor,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(color = borderColor)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📲 $phone",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor,
                    modifier = Modifier.clickable { onCallClick() }
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = onCallClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Call,
                            contentDescription = "Call",
                            tint = OsakaRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Button(
                        onClick = onWhatsappClick,
                        colors = ButtonDefaults.buttonColors(containerColor = WhatsappGreen),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(text = "WhatsApp", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}