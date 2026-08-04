package org.freedu.osakatelevison.ui.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.freedu.osakatelevison.R
import org.freedu.osakatelevison.model.SupabaseGalleryItem
import org.freedu.osakatelevison.data.viewModel.GalleryUiState
import org.freedu.osakatelevison.data.viewModel.GalleryViewModel
import org.freedu.osakatelevison.ui.theme.DarkBackground
import org.freedu.osakatelevison.ui.theme.DarkBorder
import org.freedu.osakatelevison.ui.theme.DarkForeground
import org.freedu.osakatelevison.ui.theme.DarkMutedForeground
import org.freedu.osakatelevison.ui.theme.DarkSecondary
import org.freedu.osakatelevison.ui.theme.LightBackground
import org.freedu.osakatelevison.ui.theme.LightBorder
import org.freedu.osakatelevison.ui.theme.LightForeground
import org.freedu.osakatelevison.ui.theme.LightMuted
import org.freedu.osakatelevison.ui.theme.LightMutedForeground
import org.freedu.osakatelevison.ui.theme.LightSecondary
import org.freedu.osakatelevison.ui.theme.OsakaRed
import org.freedu.osakatelevison.ui.theme.OsakaRedHover
import org.freedu.osakatelevison.ui.theme.OsakaRedLightBg

// 1. Animated Shimmer Modifier Extension
fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "galleryShimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "galleryShimmerAnimation"
    )

    val shimmerColors = listOf(
        Color(0xFFEBEBEB),
        Color(0xFFF5F5F5),
        Color(0xFFEBEBEB)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 500f, translateAnim - 500f),
        end = Offset(translateAnim, translateAnim)
    )

    this.background(brush)
}

// 2. Placeholder Card matching the exact shape of GalleryGridItem
@Composable
private fun GalleryItemShimmer(
    cardBgColor: Color,
    borderColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Image Box Shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .shimmerEffect()
            )

            // Caption Bar Shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .height(12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    viewModel: GalleryViewModel = viewModel()
) {


    val textColor = LightForeground
    val mutedTextColor = LightMutedForeground
    val cardBgColor = LightSecondary
    val borderColor =  LightBorder

    val uiState by viewModel.uiState.collectAsState()
    var selectedImageIndex by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Persistent Header Section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 4.dp, bottom = 2.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                shape = RoundedCornerShape(14.dp),
                color = OsakaRedLightBg
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Photo Gallery",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OsakaRed
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Explore Our Products & Moments",
                        fontSize = 13.sp,
                        color = OsakaRedHover
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Screen Body State Handling
        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = uiState) {
                is GalleryUiState.Loading -> {
                    // Grid of 6 Animated Shimmer Placeholders
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(6) {
                            GalleryItemShimmer(
                                cardBgColor = cardBgColor,
                                borderColor = borderColor
                            )
                        }
                    }
                }

                is GalleryUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.message, color = textColor)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.fetchGalleryImages() },
                            colors = ButtonDefaults.buttonColors(containerColor = OsakaRed),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Outlined.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Retry", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                is GalleryUiState.Success -> {
                    val images = state.items

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(images) { index, item ->
                            GalleryGridItem(
                                item = item,
                                cardBgColor = cardBgColor,
                                borderColor = borderColor,
                                textColor = textColor,
                                onClick = { selectedImageIndex = index }
                            )
                        }
                    }

                    // Full Screen Dialog Preview
                    selectedImageIndex?.let { index ->
                        val currentItem = images.getOrNull(index)
                        if (currentItem != null) {
                            FullScreenImagePreview(
                                item = currentItem,
                                onDismiss = { selectedImageIndex = null }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GalleryGridItem(
    item: SupabaseGalleryItem,
    cardBgColor: Color,
    borderColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(LightMuted)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.imageUrl)
                        .crossfade(true)
                        .error(R.drawable.aboutosaka)
                        .placeholder(R.drawable.aboutosaka)
                        .build(),
                    contentDescription = item.caption ?: "Gallery Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            item.caption?.let { captionText ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = captionText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textColor,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun FullScreenImagePreview(
    item: SupabaseGalleryItem,
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .align(Alignment.Center)
                    .padding(16.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.imageUrl)
                        .crossfade(true)
                        .error(R.drawable.aboutosaka)
                        .build(),
                    contentDescription = item.caption,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }

            item.caption?.let { captionText ->
                Text(
                    text = captionText,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                )
            }
        }
    }
}