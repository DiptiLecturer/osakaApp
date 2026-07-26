package org.freedu.osakatelevison.data


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.freedu.osakatelevison.ui.theme.OsakaRed

private val SelectedNavy = Color(0xFF131A26)
private val UnselectedBg = Color(0xFFF6F8FA)
private val UnselectedText = Color(0xFF333333)

@Composable
fun CategoryFilterSection(
    selectedMainCategory: String,
    onMainCategorySelect: (String) -> Unit,
    selectedSize: String,
    onSizeSelect: (String) -> Unit,
    selectedModel: String,
    onModelSelect: (String) -> Unit
) {
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

    // Determine current size list and model list based on active category
    val currentDataMap = when (selectedMainCategory) {
        "Television" -> tvData
        "Fan" -> fanData
        else -> emptyMap()
    }

    val availableSizes = currentDataMap.keys.toList()
    val availableModels = currentDataMap[selectedSize] ?: listOf("All Models")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. "Our Categories" Heading
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.ExtraBold)) {
                    append("Our ")
                }
                withStyle(style = SpanStyle(color = OsakaRed, fontWeight = FontWeight.ExtraBold)) {
                    append("Categories")
                }
            },
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // 2. Top Category Pills (Television, Fan, Cooker, More)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            categories.forEach { category ->
                val isSelected = category == selectedMainCategory
                Box(
                    modifier = Modifier
                        .then(
                            if (isSelected) Modifier.shadow(8.dp, RoundedCornerShape(20.dp), spotColor = OsakaRed)
                            else Modifier.shadow(2.dp, RoundedCornerShape(20.dp), spotColor = Color.LightGray)
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) OsakaRed else Color.White)
                        .clickable { onMainCategorySelect(category) }
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) Color.White else UnselectedText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // 3. Sub-filter sections (Rendered for TV and Fan)
        if (currentDataMap.isNotEmpty()) {
            val sizeTitle = if (selectedMainCategory == "Television") "PICK TV SIZE" else "PICK FAN SIZE"
            val modelTitle = if (selectedMainCategory == "Television") "PICK TV SERIES" else "PICK FAN TYPE"

            // SIZE CARD
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Text(
                        text = sizeTitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6C757D),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(availableSizes) { size ->
                            FilterPill(
                                text = size,
                                isSelected = size == selectedSize,
                                onClick = { onSizeSelect(size) }
                            )
                        }
                    }
                }
            }

            // MODEL/TYPE CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Text(
                        text = modelTitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6C757D),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(availableModels) { model ->
                            FilterPill(
                                text = model,
                                isSelected = model == selectedModel,
                                onClick = { onModelSelect(model) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterPill(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .shadow(
                elevation = if (isSelected) 4.dp else 1.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.LightGray
            )
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) SelectedNavy else UnselectedBg)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else UnselectedText,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}