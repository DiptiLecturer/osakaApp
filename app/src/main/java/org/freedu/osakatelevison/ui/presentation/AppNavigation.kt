package org.freedu.osakatelevison.ui.presentation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.freedu.osakatelevison.ui.theme.OsakaRed

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
    var showExitDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Intercept System Back Press
    BackHandler {
        if (currentScreen != BottomNavItem.Home) {
            // Rule 1: If on any other tab (Product, Gallery, etc.), back takes user to Home tab first
            currentScreen = BottomNavItem.Home
        } else {
            // Rule 2: If already on Home tab, show exit confirmation dialog
            showExitDialog = true
        }
    }

    Scaffold(
        bottomBar = {
            OsakaBottomNavigationBar(
                currentScreen = currentScreen, onTabSelected = { screen ->
                    currentScreen = screen
                })
        }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                BottomNavItem.Home -> HomeScreen(
                    onNavigateToProducts = {
                        currentScreen = BottomNavItem.Product
                    })

                BottomNavItem.Product -> ProductScreen()
                BottomNavItem.Gallery -> GalleryScreen()
                BottomNavItem.Contact -> ContactScreen()
                BottomNavItem.About -> AboutUsScreen(
                    onNavigate = { route ->
                        when (route) {
                            "all_products" -> currentScreen = BottomNavItem.Product
                            "visual_gallery" -> currentScreen = BottomNavItem.Gallery
                            "get_in_touch" -> currentScreen = BottomNavItem.Contact
                        }
                    })
            }
        }
    }

    // Exit Confirmation Dialog
    if (showExitDialog) {
        ExitConfirmationDialog(onDismiss = { showExitDialog = false }, onConfirmExit = {
            showExitDialog = false
            // Finish Activity to exit app safely
            (context as? Activity)?.finish()
        })
    }

}

@Composable
fun ExitConfirmationDialog(
    onDismiss: () -> Unit, onConfirmExit: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.background(Color.White).fillMaxWidth(),


        onDismissRequest = onDismiss, title = {
            Text(
                text = "Exit App?",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }, text = {
            Text(
                color = Color.Black,
                text = "Are you sure you want to exit Osaka Television?",
                fontSize = 14.sp
            )
        }, confirmButton = {
            Button(
                onClick = onConfirmExit,
                colors = ButtonDefaults.buttonColors(containerColor = OsakaRed)
            ) {
                Text(
                    color = Color.White, text = "Exit"
                )
            }
        }, dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(
                    color = Color.Black, text = "Cancel"
                )
            }
        })
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
}*//*
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
    currentScreen: BottomNavItem, onTabSelected: (BottomNavItem) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Product,
        BottomNavItem.Gallery,
        BottomNavItem.Contact,
        BottomNavItem.About
    )

    // Locked to crisp light theme colors
    val bgColor = Color.White
    val dividerColor = Color(0xFFEEEEEE)
    val activeColor = OsakaRed
    val inactiveColor = Color(0xFF666666)

    Surface(
        color = bgColor,
        tonalElevation = 6.dp,
        modifier = Modifier.fillMaxWidth().navigationBarsPadding()
    ) {
        Column {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = dividerColor
            )
            Row(
                modifier = Modifier.fillMaxWidth().height(60.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = currentScreen == item

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxHeight().weight(1f)
                            .clickable { onTabSelected(item) }) {
                        // Top active indicator bar
                        Box(
                            modifier = Modifier.width(28.dp).height(3.dp)
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
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) activeColor else inactiveColor
                        )

                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}