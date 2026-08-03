package org.freedu.osakatelevison.ui.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.freedu.osakatelevison.ui.theme.DarkMutedForeground
import org.freedu.osakatelevison.ui.theme.DarkSecondary
import org.freedu.osakatelevison.ui.theme.LightBackground
import org.freedu.osakatelevison.ui.theme.LightMutedForeground
import org.freedu.osakatelevison.ui.theme.OsakaRed
import org.freedu.osakatelevison.ui.theme.OsakaRedLight

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object Product : BottomNavItem("product", "Product", Icons.Default.ShoppingCart)
    object Gallery : BottomNavItem("gallery", "Gallery", Icons.Default.Collections) // Updated Icon
    object Contact : BottomNavItem("contact", "Contact", Icons.Default.Phone)
    object About : BottomNavItem("about", "About", Icons.Default.Info)
}

@Composable
fun MainAppScreen() {
    var currentScreen by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }

    Scaffold(
        bottomBar = {
            OsakaBottomNavigationBar(
                currentScreen = currentScreen, // Pass currentScreen state down
                onTabSelected = { screen ->
                    currentScreen = screen
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                BottomNavItem.Home -> HomeScreen(
                    onNavigateToProducts = {
                        currentScreen = BottomNavItem.Product // Switch screen to Product
                    }
                )
                BottomNavItem.Product -> ProductScreen()
                BottomNavItem.Gallery -> GalleryScreen()
                BottomNavItem.Contact -> ContactScreen()
                BottomNavItem.About -> AboutUsScreen()
            }
        }
    }
}/*
@Composable
fun OsakaBottomNavigationBar(
    currentScreen: BottomNavItem,
    onTabSelected: (BottomNavItem) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Product,
        BottomNavItem.Gallery,
        BottomNavItem.Contact,
        BottomNavItem.About
    )

    val isDark = isSystemInDarkTheme()
    val navBg = if (isDark) Color(0xFF1E1E1E) else Color.White
    val selectedContentColor = OsakaRed
    val unselectedContentColor = if (isDark) Color(0xFF8E8E93) else Color(0xFF707070)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = navBg,
            shadowElevation = 10.dp,
            tonalElevation = 2.dp,
            border = BorderStroke(
                width = 1.dp,
                color = if (isDark) Color(0xFF2C2C2C) else Color(0xFFEBEBEB)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = currentScreen == item

                    // Animate background color on active selection
                    val pillBgColor by animateColorAsState(
                        targetValue = if (isSelected) OsakaRed.copy(alpha = 0.12f) else Color.Transparent,
                        animationSpec = tween(300),
                        label = "pillBgColor"
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(pillBgColor)
                            .clickable { onTabSelected(item) }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = if (isSelected) selectedContentColor else unselectedContentColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.title,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) selectedContentColor else unselectedContentColor
                            )
                        }
                    }
                }
            }
        }
    }
}*/
/*
@Composable
fun OsakaBottomNavigationBar(
    currentScreen: BottomNavItem,
    onTabSelected: (BottomNavItem) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Product,
        BottomNavItem.Gallery,
        BottomNavItem.Contact,
        BottomNavItem.About
    )

    val isDark = isSystemInDarkTheme()
    val barBg = if (isDark) Color(0xFF1C1C1E) else Color.White

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = barBg,
            shadowElevation = 8.dp,
            border = BorderStroke(1.dp, if (isDark) Color(0xFF2A2A2D) else Color(0xFFF0F0F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = currentScreen == item

                    val containerColor by animateColorAsState(
                        targetValue = if (isSelected) OsakaRed else Color.Transparent,
                        animationSpec = tween(250),
                        label = "containerColor"
                    )

                    val contentColor by animateColorAsState(
                        targetValue = if (isSelected) Color.White else (if (isDark) Color.LightGray else Color.DarkGray),
                        animationSpec = tween(250),
                        label = "contentColor"
                    )

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(containerColor)
                            .clickable { onTabSelected(item) }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = contentColor,
                                modifier = Modifier.size(18.dp)
                            )

                            // Animated text expansion for selected tab
                            AnimatedVisibility(
                                visible = isSelected,
                                enter = fadeIn() + expandHorizontally(),
                                exit = fadeOut() + shrinkHorizontally()
                            ) {
                                Row {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = item.title,
                                        color = contentColor,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}*/
@Composable
fun OsakaBottomNavigationBar(
    currentScreen: BottomNavItem,
    onTabSelected: (BottomNavItem) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Product,
        BottomNavItem.Gallery,
        BottomNavItem.Contact,
        BottomNavItem.About
    )

    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212).copy(alpha = 0.95f) else Color.White.copy(alpha = 0.95f)

    Surface(
        color = bgColor,
        tonalElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Column {
            HorizontalDivider(
                Modifier,
                thickness = 1.dp,
                color = if (isDark) Color(0xFF282828) else Color(0xFFEEEEEE)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = currentScreen == item
                    val activeColor = OsakaRed
                    val inactiveColor = if (isDark) Color(0xFF888888) else Color(0xFF888888)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .clickable { onTabSelected(item) }
                    ) {
                        // Top active line indicator
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .height(3.dp)
                                .clip(RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 2.dp))
                                .background(if (isSelected) activeColor else Color.Transparent)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (isSelected) activeColor else inactiveColor,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = item.title,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) activeColor else inactiveColor
                        )

                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}