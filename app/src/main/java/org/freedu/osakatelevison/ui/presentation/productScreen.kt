package org.freedu.osakatelevison.ui.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import org.freedu.osakatelevison.R
import org.freedu.osakatelevison.model.Product
import org.freedu.osakatelevison.data.viewModel.ProductUiState
import org.freedu.osakatelevison.data.viewModel.ProductViewModel
import org.freedu.osakatelevison.ui.theme.DarkBackground
import org.freedu.osakatelevison.ui.theme.DarkForeground
import org.freedu.osakatelevison.ui.theme.LightBackground
import org.freedu.osakatelevison.ui.theme.LightForeground
import org.freedu.osakatelevison.ui.theme.OsakaRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    viewModel: ProductViewModel = viewModel(), onNavigateToDetails: ((Product) -> Unit)? = null
) {
    val isDark = isSystemInDarkTheme()
    val bgColor =  LightBackground
    val textColor =  Color.Black

    val uiState by viewModel.uiState.collectAsState()

    var selectedProductForSheet by remember { mutableStateOf<Product?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().background(color = Color.White)
            .padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        Text(
            text = "All Products",
            fontWeight = FontWeight.Bold,
            color = textColor,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )

        when (val state = uiState) {
            is ProductUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = OsakaRed)
                }
            }

            is ProductUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.message,
                        color = textColor,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.fetchProducts() },
                        colors = ButtonDefaults.buttonColors(containerColor = OsakaRed),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Retry", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            is ProductUiState.Success -> {
                // 1. Compact Search Field
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 2.dp),
                    placeholder = {
                        Text(
                            "Search fans (12, 16, 18) or TVs...",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    trailingIcon = {
                        if (state.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OsakaRed,
                        unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                        focusedContainerColor =  Color(0xFFF8F8F8),
                        unfocusedContainerColor =Color(0xFFF8F8F8)
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 2. Compact Category Bar
                CompactCategoryBar(
                    categories = state.categories,
                    selectedCategory = state.selectedCategory,
                    onCategorySelected = { viewModel.selectCategory(it) },
                    isDark = isDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 3. Products Grid View
                if (state.products.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No products found in this category.",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.products) { product ->
                            ProductCard(
                                product = product, isDark = isDark, onClick = {
                                    selectedProductForSheet = product
                                    onNavigateToDetails?.invoke(product)
                                })
                        }
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet Preview
    selectedProductForSheet?.let { product ->
        ProductDetailBottomSheet(
            product = product, isDark = isDark, onDismiss = { selectedProductForSheet = null })
    }
}

@Composable
fun ProductCard(
    product: Product, isDark: Boolean = isSystemInDarkTheme(), onClick: () -> Unit
) {
    val cardBg =  Color.White
    val textColor = Color.Black
    val imageBg = Color.White

    val sizeText = product.size.trim() ?: ""
    val isFan = sizeText in listOf("12", "16", "18", "12\"", "16\"", "18\"") || listOf(
        "12",
        "16",
        "18"
    ).any { product.name.contains(it) }

    val categoryTag = if (isFan) "FAN" else "TV"
    val categoryIcon = if (isFan) Icons.Default.Air else Icons.Default.Tv

    // Changed Fan background to Green
    val tagBackgroundColor = if (isFan) Color(0xFF2E7D32) else Color(0xFF1976D2)

    Card(
        modifier = Modifier.fillMaxWidth().wrapContentHeight().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp).background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            // Compressed Image Box
            Box(
                modifier = Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(8.dp))
                    .background(imageBg)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(product.imageUrl)
                        .crossfade(true).error(R.drawable.aboutosaka)
                        .placeholder(R.drawable.aboutosaka).build(),
                    contentDescription = product.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize().padding(6.dp)
                )

                // Compact Badge with Green Background for FAN
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier.padding(6.dp).align(Alignment.TopStart)
                        .clip(RoundedCornerShape(4.dp))
                        .background(tagBackgroundColor)
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = categoryIcon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(10.dp)
                    )
                    Text(
                        text = categoryTag,
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Title
            Text(
                text = product.name,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 14.sp,
                modifier = Modifier.heightIn(min = 28.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Price Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "MRP ",
                    fontSize = 9.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "৳${product.price.toInt()}",
                    fontSize = 12.sp,
                    color = OsakaRed,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // CTA Button
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor =  Color.Black,
                    contentColor =   Color.White
                ),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth().height(30.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "EXPLORE NOW",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.4.sp
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailBottomSheet(
    product: Product, isDark: Boolean, onDismiss: () -> Unit
) {
    val sheetBg =  Color.White
    val textColor =  Color.Black

    // Forces the sheet to open expanded without requiring the user to drag up
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss, sheetState = sheetState, containerColor = sheetBg
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(260.dp).clip(RoundedCornerShape(16.dp))
                    .background( Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = product.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "MRP: ৳${product.price.toInt()}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = OsakaRed
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = OsakaRed),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Close Details", color = Color.White)
            }
        }
    }
}

@Composable
fun CompactCategoryBar(
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit,
    isDark: Boolean
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(categories) { category ->
            val isSelected = category == selectedCategory

            // Selected: Red background / Unselected: White background
            val containerColor = if (isSelected) Color.Black else Color.White

            // Selected: White text / Unselected: Black text
            val textColor = if (isSelected) Color.White else Color.Black

            Box(
                modifier = Modifier
                    .height(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(containerColor)
                    // Border added so unselected white buttons are visible on white backgrounds
                    .border(
                        width = 1.dp,
                        color = if (isSelected) Color.White else Color(0xFFE0E0E0),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onCategorySelected(category) }
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = textColor,
                    maxLines = 1
                )
            }
        }
    }
}

