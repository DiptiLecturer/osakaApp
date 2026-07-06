package org.freedu.osakatelevison

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.freedu.osakatelevison.ui.presentation.MainAppScreen
import org.freedu.osakatelevison.ui.theme.OsakaTelevisonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OsakaTelevisonTheme {
                MainAppScreen()
            }
        }
    }
}