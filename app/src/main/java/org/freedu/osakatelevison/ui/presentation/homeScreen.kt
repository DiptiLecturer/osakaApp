package org.freedu.osakatelevison.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import org.freedu.osakatelevison.ui.theme.LightMutedForeground
import org.freedu.osakatelevison.ui.theme.OsakaRed
import kotlin.math.abs

@Preview(showSystemUi = true)
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,


        ) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            text = "Home Screen Content",
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
        )

    }
    Spacer(Modifier.height(16.dp))
    WebsiteCarousel()


}


@Composable
fun WebsiteCarousel() {
    val images = listOf(
        "https://picsum.photos/id/10/800/400",
        "https://picsum.photos/id/11/800/400",
        "https://picsum.photos/id/12/800/400"
    )

    val pagerState = rememberPagerState(pageCount = { images.size })

    // Fixed Auto-Scroll: We trigger based on a true loop instead of resetting on every page change
    LaunchedEffect(key1 = true) {
        while (true) {
            delay(2000)
            val nextPage = (pagerState.currentPage + 1) % images.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                // Calculate how far this specific page is from the center screen
                val pageOffset =
                    ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)

                // Absolute offset value used for custom alpha/fade curves
                val absOffset = abs(pageOffset)

                AsyncImage(
                    model = images[page],
                    contentDescription = "Carousel Image $page",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            // 1. Smooth Fade Effect: decrease opacity as it moves away from center
                            alpha = 1f - absOffset.coerceIn(0f, 1f)

                            // 2. Parallax/Translation Fix: keeps the fading images layered cleanly
                            // over each other rather than roughly snapping halfway
                            translationX = pageOffset * size.width
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Dot Indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(images.size) { iteration ->
                val isSelected = pagerState.currentPage == iteration

                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (isSelected) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            color = if (isSelected) OsakaRed else LightMutedForeground.copy(alpha = 0.5f)
                        )
                )
            }
        }
    }
}