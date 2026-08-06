package org.freedu.osakatelevison.ui.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationMapScreen() {
    // 1. Coordinates provided by you
    val corporateOffice = LatLng(23.75385, 90.35974) // Kaderabad Housing
    val wholesaleCenter = LatLng(23.7223, 90.41311)  // Kaptan Bazar
    val salesCenter = LatLng(23.7388, 90.3857)       // Multiplan (Elephant Road)

    // Initial camera centered near Dhaka
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(23.7400, 90.3850), 12f)
    }

    // Automatically zoom/fit all 3 markers into screen view on load
    LaunchedEffect(Unit) {
        val boundsBuilder = LatLngBounds.builder().apply {
            include(corporateOffice)
            include(wholesaleCenter)
            include(salesCenter)
        }
        val bounds = boundsBuilder.build()
        // 100px padding around the map edges
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngBounds(bounds, 100)
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // Marker 1: Corporate Office
        Marker(
            state = MarkerState(position = corporateOffice),
            title = "Corporate Office",
            snippet = "Kaderabad Housing, Mohammadpur"
        )

        // Marker 2: Wholesale Center
        Marker(
            state = MarkerState(position = wholesaleCenter),
            title = "Wholesale Center",
            snippet = "Kaptan Bazar, Gulistan"
        )

        // Marker 3: Sales Center
        Marker(
            state = MarkerState(position = salesCenter),
            title = "Sales Center",
            snippet = "Multiplan Center, Elephant Road"
        )
    }
}