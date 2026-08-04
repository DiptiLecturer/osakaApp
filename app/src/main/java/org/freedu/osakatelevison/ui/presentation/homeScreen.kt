package org.freedu.osakatelevison.ui.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.freedu.osakatelevison.R
import org.freedu.osakatelevison.data.viewModel.HomeUiState
import org.freedu.osakatelevison.data.viewModel.HomeViewModel
import org.freedu.osakatelevison.model.HeroSlide
import org.freedu.osakatelevison.model.Product
import org.freedu.osakatelevison.ui.theme.OsakaRed
import kotlin.math.abs
import kotlin.time.Duration.Companion.milliseconds

// 1. Shimmer Extension Modifier for Carousel
fun Modifier.carouselShimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "carouselShimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f, targetValue = 1000f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ), label = "carouselShimmerAnimation"
    )

    val shimmerColors = listOf(
        Color(0xFFEBEBEB), Color(0xFFF5F5F5), Color(0xFFEBEBEB)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 500f, translateAnim - 500f),
        end = Offset(translateAnim, translateAnim)
    )

    this.background(brush)
}

// 2. Carousel Shimmer Placeholder
@Composable
fun WebsiteCarouselShimmer() {
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Carousel Container Shimmer (with 32.dp side padding matching actual carousel)
        Box(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp).height(200.dp)
                .clip(RoundedCornerShape(16.dp)).carouselShimmerEffect()
        ) {
            // Text Line Shimmer at Bottom Left
            Column(
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(0.5f).height(14.dp)
                        .clip(RoundedCornerShape(4.dp)).carouselShimmerEffect()
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(0.35f).height(10.dp)
                        .clip(RoundedCornerShape(4.dp)).carouselShimmerEffect()
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Indicator Bar Shimmer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier.padding(horizontal = 3.dp).height(8.dp)
                        .width(if (index == 0) 20.dp else 8.dp).clip(CircleShape)
                        .carouselShimmerEffect()
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onNavigateToDetails: ((Product) -> Unit)? = null,
    onNavigateToProducts: (() -> Unit)? = null
) {
    val scrollState = rememberScrollState()
    val heroSlidesState by viewModel.heroSlidesState.collectAsState()

    val highlightedProducts by viewModel.filteredHighlights.collectAsState()
    val isHighlightsLoading by viewModel.isHighlightsLoading.collectAsState()
    val selectedHighlightTab by viewModel.selectedHighlightTab.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(16.dp))

        // Hero Carousel State Handling
        when (val state = heroSlidesState) {
            is HomeUiState.Loading -> {
                // Show Carousel Shimmer Effect during loading
                WebsiteCarouselShimmer()
            }

            is HomeUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { viewModel.loadHomeScreenData() },
                        colors = ButtonDefaults.buttonColors(containerColor = OsakaRed)
                    ) {
                        Text("Retry", fontSize = 12.sp)
                    }
                }
            }

            is HomeUiState.Success -> {
                WebsiteCarousel(slides = state.heroSlides)
            }
        }

        Spacer(Modifier.height(20.dp))

        // Product Highlights Section
        ProductHighlightsSection(
            products = highlightedProducts,
            isLoading = isHighlightsLoading,
            selectedTab = selectedHighlightTab,
            onTabSelected = { viewModel.selectHighlightTab(it) },
            onProductClick = { product -> onNavigateToDetails?.invoke(product) },
            onViewAllClick = { onNavigateToProducts?.invoke() })
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
                page = nextPage, animationSpec = tween(durationMillis = 600)
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().height(200.dp),
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 12.dp
        ) { page ->
            val slide = slides[page]

            val pageOffset =
                ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
            val absOffset = abs(pageOffset).coerceIn(0f, 1f)
            val scale = 1f - (absOffset * 0.12f)
            val alpha = 1f - (absOffset * 0.3f)

            Card(
                shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxSize().graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(slide.imageUrl)
                            .crossfade(true).error(R.drawable.aboutosaka)
                            .placeholder(R.drawable.aboutosaka).build(),
                        contentDescription = slide.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier.fillMaxSize().background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent, Color.Black.copy(alpha = 0.75f)
                                    ), startY = 60f
                                )
                            )
                    )

                    Column(
                        modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(slides.size) { iteration ->
                val isSelected = pagerState.currentPage == iteration
                val width = if (isSelected) 20.dp else 8.dp

                Box(
                    modifier = Modifier.padding(horizontal = 3.dp).height(8.dp).width(width)
                        .clip(CircleShape).background(
                            color = if (isSelected) OsakaRed else Color.Gray.copy(alpha = 0.4f)
                        )
                )
            }
        }
    }
}

