package org.freedu.osakatelevison.ui.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import org.freedu.osakatelevison.ui.theme.LightMutedForeground
import org.freedu.osakatelevison.ui.theme.OsakaRed

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object Product : BottomNavItem("product", "Product", Icons.Default.ShoppingCart)
    object Gallery :
        BottomNavItem("gallery", "Gallery", Icons.Default.MailOutline) // Or custom gallery icon

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
                BottomNavItem.Product -> OsakaProductScreen()
                BottomNavItem.Gallery -> Text("Photo/Video Gallery")
                BottomNavItem.Contact -> Text("Contact Form & Details")
                BottomNavItem.About -> Text("About Osaka TV Details")
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

    NavigationBar {
        items.forEach { item ->
            val isSelected = selectedItem == item

            NavigationBarItem(
                selected = isSelected, onClick = {
                selectedItem = item
                onTabSelected(item)
            }, label = { Text(text = item.title) }, icon = {
                Icon(
                    imageVector = item.icon, contentDescription = item.title
                )
            }, colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OsakaRed,
                selectedTextColor = OsakaRed,
                indicatorColor = org.freedu.osakatelevison.ui.theme.OsakaRedLight,
                unselectedIconColor = LightMutedForeground,
                unselectedTextColor = LightMutedForeground
            )
            )
        }
    }
}