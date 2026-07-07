package org.freedu.osakatelevison.ui.presentation

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import org.freedu.osakatelevison.ui.theme.LightMutedForeground
import org.freedu.osakatelevison.ui.theme.OsakaRed
import kotlin.math.abs
import kotlin.time.Duration.Companion.milliseconds

@Preview(showSystemUi = true)
@Composable

fun HomeScreen() {
    // Added rememberScrollState() and verticalScroll to make the screen scrollable
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()

            .background(Color.White)
            .verticalScroll(scrollState), // Allows scrolling past the carousel to see the grid
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 2. Carousel Section
        Spacer(Modifier.height(26.dp))
        WebsiteCarousel()

        // 3. Newly Arrived Section (Now safely inside the Column right below the carousel)
        Spacer(Modifier.height(24.dp))
        NewlyArrivedSection()
    }
}


@Composable
fun WebsiteCarousel() {
// Updated with 2 TV images and 1 Fan image placeholder URLs
    val images = listOf(
        "https://images.unsplash.com/photo-1593305841991-05c297ba4575?w=800&auto=format&fit=crop", // TV 1
        "https://images.unsplash.com/photo-1593784991095-a205069470b6?w=800&auto=format&fit=crop", // TV 2
        "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=800&auto=format&fit=crop" // Electric Fan
    )

    val pagerState = rememberPagerState(pageCount = { images.size })

/*    LaunchedEffect(key1 = true) {
        while (true) {
            delay(2000.milliseconds)
            val nextPage = (pagerState.currentPage + 1) % images.size
            pagerState.animateScrollToPage(nextPage)
        }
    }*/

    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(120.dp)
        ) {
            HorizontalPager(
                state = pagerState, modifier = Modifier.fillMaxSize()
            ) { page ->
                val pageOffset =
                    ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
                val absOffset = abs(pageOffset)

                AsyncImage(
                    model = images[page],
                    contentDescription = "Carousel Image $page",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().graphicsLayer {
                            // 1. Smooth Fade Effect: decrease opacity as it moves away from center
                            alpha = 1f - absOffset.coerceIn(0f, 1f)
                            translationX = pageOffset * size.width
                        })
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Dot Indicators
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(images.size) { iteration ->
                val isSelected = pagerState.currentPage == iteration

                Box(
                    modifier = Modifier.padding(horizontal = 4.dp)
                        .size(if (isSelected) 10.dp else 8.dp).clip(CircleShape).background(
                            color = if (isSelected) OsakaRed else LightMutedForeground.copy(alpha = 0.5f)
                        )
                )
            }
        }
    }
}


data class NewArrivalItem(
    val title: String,
    val price: String,
    val imageUrl: String
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
        ),
        NewArrivalItem(
            title = "Basic Double Glass - 24\" Basic Double Glass",
            price = "11,000 ৳",
            imageUrl = "https://images.unsplash.com/photo-1593784991095-a205069470b6?w=500&auto=format&fit=crop" // TV 2
        ),
        NewArrivalItem(
            title = "Premium Stand Fan - High Speed Cooling",
            price = "4,500 ৳",
            imageUrl = "https://images.unsplash.com/photo-1593784991095-a205069470b6?w=800&auto=format&fit=crop" // Fan 1
        ),
        NewArrivalItem(
            title = "Smart Frameless - 32\" Android LED",
            price = "18,500 ৳",
            imageUrl = "https://images.unsplash.com/photo-1560169897-fc0cdbdfa4d5?w=500&auto=format&fit=crop" // TV 3
        ),
        NewArrivalItem(
            title = "Vintage Table Fan - Metal Finish",
            price = "3,200 ৳",
            imageUrl = "https://images.unsplash.com/photo-1593784991095-a205069470b6?w=800&auto=format&fit=crop" // Fan 2
        ),
        NewArrivalItem(
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
            modifier = Modifier.clip(RoundedCornerShape(50))
                .background(FlashReleaseBg)
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
        modifier = Modifier
            .width(190.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                // Replaced placeholder Box with live AsyncImage
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )

                // "NEW" Red Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(8.dp))
                        .background(OsakaRed)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
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
                    text = "MRP ",
                    fontSize = 10.sp,
                    color = OsakaRed,
                    fontWeight = FontWeight.Bold
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
