package org.freedu.osakatelevison.ui.presentation

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.freedu.osakatelevison.R
import org.freedu.osakatelevison.data.HeroSlide
import org.freedu.osakatelevison.data.HomeUiState
import org.freedu.osakatelevison.data.HomeViewModel
import org.freedu.osakatelevison.ui.theme.LightMutedForeground
import org.freedu.osakatelevison.ui.theme.OsakaRed
import kotlin.math.abs
import kotlin.time.Duration.Companion.milliseconds

@Preview(showSystemUi = true)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(16.dp))

        when (val state = uiState) {
            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = OsakaRed)
                }
            }

            is HomeUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Failed to load hero slides", color = Color.Gray)
                }
            }

            is HomeUiState.Success -> {
                WebsiteCarousel(slides = state.heroSlides)
            }
        }

        Spacer(Modifier.height(24.dp))
        NewlyArrivedSection()
    }
}

@Composable
fun WebsiteCarousel(slides: List<HeroSlide>) {
    if (slides.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { slides.size })

    // Fixed Auto-scroll loop using settledPage snapshot
    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.settledPage }.collectLatest { page ->
            delay(1000.milliseconds)
            val nextPage = (page + 1) % slides.size
            pagerState.animateScrollToPage(
                page = nextPage,
                animationSpec = tween(durationMillis = 600)
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Content padding allows seeing previous & next cards on sides
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 12.dp
        ) { page ->
            val slide = slides[page]

            // Calculate scale factor for card pop-out effect
            val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
            val absOffset = abs(pageOffset).coerceIn(0f, 1f)
            val scale = 1f - (absOffset * 0.12f) // Active card scales up to 100%, inactive down to 88%
            val alpha = 1f - (absOffset * 0.3f)   // Mild dimming on inactive peek cards

            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(slide.imageUrl)
                            .crossfade(true)
                            .error(R.drawable.aboutosaka)
                            .placeholder(R.drawable.aboutosaka)
                            .build(),
                        contentDescription = slide.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient Overlay for Text Visibility
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.75f)
                                    ),
                                    startY = 60f
                                )
                            )
                    )

                    // Text correctly anchored to Bottom Left corner
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = slide.title,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        slide.description?.let { desc ->
                            Text(
                                text = desc,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Animated Indicators (expanding pill for active card)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(slides.size) { iteration ->
                val isSelected = pagerState.currentPage == iteration
                val width = if (isSelected) 20.dp else 8.dp

                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .height(8.dp)
                        .width(width)
                        .clip(CircleShape)
                        .background(
                            color = if (isSelected) OsakaRed else Color.Gray.copy(alpha = 0.4f)
                        )
                )
            }
        }
    }
}
data class NewArrivalItem(
    val title: String, val price: String, val imageUrl: String
)

val FlashReleaseBg = Color(0xFFFFEAEA)
val OsakaRed = Color(0xFFE60000)
val TextDark = Color(0xFF1A1A1A)
val TextMuted = Color(0xFF555555)

@Composable
fun NewlyArrivedSection(modifier: Modifier = Modifier) {
    // Mock Data mimicking your screenshot
    val sampleItems = listOf(
        NewArrivalItem(
            title = "Smart Frameless - 24\" Smart Android Frameless",
            price = "11,500 ৳",
            imageUrl = "https://images.unsplash.com/photo-1593305841991-05c297ba4575?w=500&auto=format&fit=crop" // TV 1
        ), NewArrivalItem(
            title = "Basic Double Glass - 24\" Basic Double Glass",
            price = "11,000 ৳",
            imageUrl = "https://images.unsplash.com/photo-1593784991095-a205069470b6?w=500&auto=format&fit=crop" // TV 2
        ), NewArrivalItem(
            title = "Premium Stand Fan - High Speed Cooling",
            price = "4,500 ৳",
            imageUrl = "https://images.unsplash.com/photo-1593784991095-a205069470b6?w=800&auto=format&fit=crop" // Fan 1
        ), NewArrivalItem(
            title = "Smart Frameless - 32\" Android LED",
            price = "18,500 ৳",
            imageUrl = "https://images.unsplash.com/photo-1560169897-fc0cdbdfa4d5?w=500&auto=format&fit=crop" // TV 3
        ), NewArrivalItem(
            title = "Vintage Table Fan - Metal Finish",
            price = "3,200 ৳",
            imageUrl = "https://images.unsplash.com/photo-1593784991095-a205069470b6?w=800&auto=format&fit=crop" // Fan 2
        ), NewArrivalItem(
            title = "Basic Double Glass - 40\" Full HD TV",
            price = "24,000 ৳",
            imageUrl = "https://images.unsplash.com/photo-1560169897-fc0cdbdfa4d5?w=500&auto=format&fit=crop" // TV 4
        )
    )

    Column(
        modifier = modifier.fillMaxWidth()
            .background(Color(0xFFFFF9F9)) // Slight pinkish/white gradient tint from image
            .padding(vertical = 24.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Badge Header
        Box(
            modifier = Modifier.clip(RoundedCornerShape(50)).background(FlashReleaseBg)
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = "FLASH RELEASE",
                color = OsakaRed,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. Main Title
        Row {
            Text(
                text = "Newly ", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextDark
            )
            Text(
                text = "Arrived", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = OsakaRed
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 3. Subtitle
        Text(
            text = "Check out our latest products just added to the store!",
            fontSize = 14.sp,
            color = TextMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Product List (LazyRow for easy side-by-side browsing)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(sampleItems) { item ->
                ProductCard(item = item)
            }
        }
    }
}

@Composable
fun ProductCard(item: NewArrivalItem) {
    Card(
        modifier = Modifier.width(190.dp).wrapContentHeight(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.fillMaxWidth().height(130.dp)
            ) {
                // Replaced placeholder Box with live AsyncImage
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize().padding(8.dp)
                )

                // "NEW" Red Badge
                Box(
                    modifier = Modifier.align(Alignment.TopStart).clip(RoundedCornerShape(8.dp))
                        .background(OsakaRed).padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "NEW",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Product Title
            Text(
                text = item.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.height(36.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Price Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "MRP ", fontSize = 10.sp, color = OsakaRed, fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.price,
                    fontSize = 14.sp,
                    color = OsakaRed,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Explore Now Button
            Button(
                onClick = { /* Handle Navigate/Details */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                Text(
                    text = "EXPLORE NOW",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
