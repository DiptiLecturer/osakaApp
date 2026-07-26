package org.freedu.osakatelevison.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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
            OsakaBottomNavigationBar(onTabSelected = { screen ->
                currentScreen = screen
            })
        }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {

            when (currentScreen) {
                BottomNavItem.Home -> HomeScreen()
                BottomNavItem.Product -> ProductScreen()
                BottomNavItem.Gallery -> GalleryScreen()
                BottomNavItem.Contact -> ContactScreen()
                BottomNavItem.About ->AboutUsScreen()

            }
        }
    }
}


@Composable
fun OsakaBottomNavigationBar(onTabSelected: (BottomNavItem) -> Unit) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Product,
        BottomNavItem.Gallery,
        BottomNavItem.Contact,
        BottomNavItem.About
    )

    var selectedItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }


    val isDarkTheme = isSystemInDarkTheme()

    val surfaceColor = if (isDarkTheme) DarkSecondary else LightBackground
    val unselectedColor = if (isDarkTheme) DarkMutedForeground else LightMutedForeground

    val indicatorColor = if (isDarkTheme) Color(0xFF451A1A) else OsakaRedLight
    Box(
        modifier = Modifier.fillMaxWidth().navigationBarsPadding()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Surface(
            tonalElevation = 0.dp,
            shadowElevation = 6.dp,
            shape = CircleShape,
            color = surfaceColor,
            modifier = Modifier.fillMaxWidth()
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 6.dp).background(Color.White)
            ) {
                items.forEach { item ->
                    val isSelected = selectedItem == item

                    NavigationBarItem(
                        selected = isSelected, onClick = {
                        selectedItem = item
                        onTabSelected(item)
                    }, label = {
                        Text(
                            text = item.title, style = MaterialTheme.typography.labelSmall
                        )
                    }, icon = {
                        Icon(
                            imageVector = item.icon, contentDescription = item.title
                        )
                    }, colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OsakaRed,
                        selectedTextColor = OsakaRed,
                        indicatorColor = indicatorColor,
                        unselectedIconColor = unselectedColor,
                        unselectedTextColor = unselectedColor
                    )
                    )
                }
            }
        }
    }
}