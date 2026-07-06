package org.freedu.osakatelevison.ui.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.freedu.osakatelevison.ui.theme.*
import androidx.core.net.toUri

@Composable
fun OsakaProductScreen() {
    val context = LocalContext.current
    val phoneNumber = "01886469096"

    // --- State Tracking ---
    var selectedCategory by remember { mutableStateOf("Television") }
    var selectedSize by remember { mutableStateOf("") }
    var selectedSeries by remember { mutableStateOf("") }

    // --- Main Category List ---
    val categories = listOf("Television", "Fan", "Cooker", "More")

    // --- TV Size -> Series/Model Data Matrix ---
    val tvData = mapOf(
        "24 inch" to listOf(
            "All Models",
            "Basic Frameless",
            "Basic Double Glass",
            "Smart Frameless",
            "Smart Double Glass",
            "Regular Series",
            "Gold Series",
            "Google TV"
        ),
        "32 inch" to listOf("All Models", "Regular Series", "Gold Series", "Google TV"),
        "43 inch" to listOf("All Models", "Regular Series", "Gold Series", "Google TV"),
        "50 inch" to listOf("All Models", "Regular Series", "Gold Series", "Google TV"),
        "65 inch" to listOf("All Models", "Regular Series", "Gold Series", "Google TV")
    )

    // --- Fan Size -> Type Data Matrix ---
    val fanData = mapOf(
        "12 inch" to listOf("All Models", "Table Fan - Rechargeable"),
        "16 inch" to listOf("All Models", "Table Fan - Rechargeable", "Stand Fan - Rechargeable"),
        "18 inch" to listOf("All Models", "Stand Fan - Rechargeable")
    )

    // --- Resolve available sizes based on selected category ---
    val availableSizes = when (selectedCategory) {
        "Television" -> tvData.keys.toList()
        "Fan" -> fanData.keys.toList()
        else -> emptyList()
    }

    // Reset/default size whenever the main category changes
    LaunchedEffect(selectedCategory) {
        selectedSize = availableSizes.firstOrNull().orEmpty()
    }

    // --- Resolve available series/types based on selected size ---
    val availableSeries = when (selectedCategory) {
        "Television" -> tvData[selectedSize] ?: emptyList()
        "Fan" -> fanData[selectedSize] ?: emptyList()
        else -> emptyList()
    }

    // Reset/default series whenever the size changes
    LaunchedEffect(selectedSize) {
        selectedSeries = availableSeries.firstOrNull().orEmpty()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // --- Header Text ---
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Our ",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = LightForeground
                )
                Text(
                    text = "Categories",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = OsakaRed
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Top Main Categories Row ---
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 2.dp)
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = if (isSelected) 4.dp else 1.dp,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .background(
                                color = if (isSelected) OsakaRed else Color.White,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color.White else LightForeground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Render content conditionally based on category ---
            if (selectedCategory == "Television" || selectedCategory == "Fan") {

                // --- PICK SIZE CARD ---
                val sizeTitle = if (selectedCategory == "Television") "PICK TV SIZE" else "PICK FAN SIZE"
                SelectionCardContainer(title = sizeTitle) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalFadingEdge(edgeWidth = 16.dp), // Subtle fade-out trick
                        contentPadding = PaddingValues(horizontal = 14.dp) // Matches outer padding beautifully
                    ) {
                        items(availableSizes) { size ->
                            val isSelected = size == selectedSize
                            FilterCapsule(
                                text = size,
                                isSelected = isSelected,
                                onClick = { selectedSize = size }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // --- PICK SERIES / TYPE CARD ---
                val seriesTitle = if (selectedCategory == "Television") "PICK TV SERIES" else "PICK FAN TYPE"
                SelectionCardContainer(title = seriesTitle) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalFadingEdge(edgeWidth = 16.dp), // Subtle fade-out trick
                        contentPadding = PaddingValues(horizontal = 14.dp)
                    ) {
                        items(availableSeries) { series ->
                            val isSelected = series == selectedSeries
                            FilterCapsule(
                                text = series,
                                isSelected = isSelected,
                                onClick = { selectedSeries = series }
                            )
                        }
                    }
                }
            } else {
                // --- COMING SOON CARD FOR COOKER & MORE ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .shadow(3.dp, RoundedCornerShape(20.dp))
                        .background(Color.White, RoundedCornerShape(20.dp))
                        .border(1.dp, LightBorder.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .padding(vertical = 40.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "📦",
                            fontSize = 36.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "$selectedCategory Section Coming Soon!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = LightMutedForeground,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "We are currently updating our inventory.",
                            fontSize = 12.sp,
                            color = LightMutedForeground.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }

        // --- FLOATING ACTION BUTTONS (FAB) ---
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // WhatsApp Button
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .shadow(3.dp, CircleShape)
                    .background(WhatsappGreen, CircleShape)
                    .clickable {
                        // Convert local number (01886458285) to international format for wa.me
                        val internationalNumber = "880" + phoneNumber.removePrefix("0")
                        try {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                "https://wa.me/$internationalNumber".toUri()
                            )
                            context.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "💬", color = Color.White, fontSize = 20.sp)
            }

            // Phone Call Button
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .shadow(3.dp, CircleShape)
                    .background(Color.Black, CircleShape)
                    .clickable {
                        try {
                            val intent = Intent(Intent.ACTION_DIAL, "tel:$phoneNumber".toUri())
                            context.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(context, "No dialer app found", Toast.LENGTH_SHORT).show()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call Support",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

// --- REUSABLE CARD WRAPPER (PADDING ADJUSTED FOR CLIP-TO-PADDING UX) ---
@Composable
fun SelectionCardContainer(
    title: String,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(20.dp))
            .background(Color.White, RoundedCornerShape(20.dp))
            .border(1.dp, LightBorder.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .padding(vertical = 12.dp) // Removed horizontal padding here so content reaches edges smoothly
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = LightMutedForeground,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(horizontal = 14.dp) // Keeping title constrained
            )
            content()
        }
    }
}

// --- REUSABLE BADGE CAPSULE ---
@Composable
fun FilterCapsule(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .shadow(
                elevation = if (isSelected) 2.dp else 0.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = if (isSelected) LightPrimary else LightSecondary,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) LightPrimary else LightBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) LightPrimaryForeground else LightForeground,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            fontSize = 13.sp
        )
    }
}

// --- CUSTOM MODIFIER FOR SUBTLE HORIZONTAL FADING EDGES ---
fun Modifier.horizontalFadingEdge(edgeWidth: Dp): Modifier = this
    .graphicsLayer(compositingStrategy = androidx.compose.ui.graphics.CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        val edgeWidthPx = edgeWidth.toPx()
        val width = size.width

        // Left fading edge mask
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(Color.Transparent, Color.Black),
                startX = 0f,
                endX = edgeWidthPx
            ),
            blendMode = BlendMode.DstIn
        )
        // Right fading edge mask
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(Color.Black, Color.Transparent),
                startX = width - edgeWidthPx,
                endX = width
            ),
            blendMode = BlendMode.DstIn
        )
    }

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OsakaProductScreenPreview() {
    OsakaProductScreen()
}