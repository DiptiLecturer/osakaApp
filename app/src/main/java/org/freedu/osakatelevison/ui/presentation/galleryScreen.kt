package org.freedu.osakatelevison.ui.presentation


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.freedu.osakatelevison.R
import org.freedu.osakatelevison.ui.theme.*

// Data class representing an item in the gallery
data class GalleryItem(
    val id: Int,
    val title: String,
    val imageUrl: String? = null // API image URL
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    // Pass API images list here. Defaults to 8 items testing local fallback 'aboutosaka.jpeg'
    apiImages: List<GalleryItem> = List(8) { index ->
        GalleryItem(
            id = index + 1,
            title = "OSAKA Display #${index + 1}",
            imageUrl = null // Set your actual API URL string here when available
        )
    }
) {
    val isDark = isSystemInDarkTheme()

    // Dynamic color tokens matching color.kt
    val bgColor = if (isDark) DarkBackground else LightBackground
    val textColor = if (isDark) DarkForeground else LightForeground
    val mutedTextColor = if (isDark) DarkMutedForeground else LightMutedForeground
    val cardBgColor = if (isDark) DarkSecondary else LightSecondary
    val borderColor = if (isDark) DarkBorder else LightBorder

    // Selected image state for Full-Screen Preview Dialog
    var selectedImageIndex by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Collections,
                            contentDescription = null,
                            tint = OsakaRed,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Photo Gallery",
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = bgColor
                )
            )
        },
        containerColor = bgColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Header / Subtitle Section
            Text(
                text = "Showroom & Product Highlights",
                fontSize = 14.sp,
                color = mutedTextColor,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Responsive 2-Column Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(apiImages) { index, item ->
                    GalleryGridItem(
                        item = item,
                        cardBgColor = cardBgColor,
                        borderColor = borderColor,
                        textColor = textColor,
                        onClick = { selectedImageIndex = index }
                    )
                }
            }
        }
    }

    // Full Screen Zoom/Preview Dialog
    selectedImageIndex?.let { index ->
        val currentItem = apiImages.getOrNull(index)
        if (currentItem != null) {
            FullScreenImagePreview(
                item = currentItem,
                onDismiss = { selectedImageIndex = null }
            )
        }
    }
}

@Composable
private fun GalleryGridItem(
    item: GalleryItem,
    cardBgColor: Color,
    borderColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f) // Keeps proportional square-like card aspect
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(LightMuted)
            ) {
                if (!item.imageUrl.isNull_or_Empty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.imageUrl)
                            .crossfade(true)
                            // Local fallback to 'aboutosaka' if remote image fails
                            .error(R.drawable.aboutosaka)
                            .placeholder(R.drawable.aboutosaka)
                            .build(),
                        contentDescription = item.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Default check with local drawables image (aboutosaka.jpeg)
                    Image(
                        painter = painterResource(id = R.drawable.aboutosaka),
                        contentDescription = item.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Caption Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = item.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor,
                    maxLines = 1
                )
            }
        }
    }
}

// Extension to check for empty strings safely
private fun String?.isNull_or_Empty(): Boolean = this == null || this.trim().isEmpty()

@Composable
private fun FullScreenImagePreview(
    item: GalleryItem,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.92f))
        ) {
            // Dismiss Button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Image Display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .align(Alignment.Center)
                    .padding(16.dp)
            ) {
                if (!item.imageUrl.isNull_or_Empty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.imageUrl)
                            .crossfade(true)
                            .error(R.drawable.aboutosaka)
                            .build(),
                        contentDescription = item.title,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.aboutosaka),
                        contentDescription = item.title,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Title Footer
            Text(
                text = item.title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            )
        }
    }
}