package com.zanoapps.map.presentation.map

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.map.domain.model.PropertyMarker

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LeafletMapView(
    modifier: Modifier = Modifier,
    markers: List<PropertyMarker>,
    properties: List<BalkanEstateProperty>,
    mapLayerType: MapLayerType,
    isDrawingMode: Boolean,
    show3DBuildings: Boolean,
    cameraLatitude: Double,
    cameraLongitude: Double,
    cameraZoom: Float,
    onMarkerClick: (String) -> Unit,
    onMapMoved: (Double, Double, Float) -> Unit,
    onAreaDrawn: (Double, Double, Double, Double) -> Unit
) {
    val context = LocalContext.current

    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.allowFileAccess = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true

            webViewClient = WebViewClient()

            // Add JavaScript interface
            addJavascriptInterface(
                MapJavaScriptInterface(
                    onMarkerClick = onMarkerClick,
                    onMapMoved = onMapMoved,
                    onAreaDrawn = onAreaDrawn
                ),
                "Android"
            )

            loadUrl("file:///android_asset/leaflet_map.html")
        }
    }

    // Update markers when they change
    LaunchedEffect(markers) {
        webView.evaluateJavascript("clearMarkers();", null)
        markers.forEach { marker ->
            val property = properties.find { it.id == marker.id }
            val price = property?.price ?: 0.0
            val currency = property?.currency ?: "€"
            val title = marker.title.replace("'", "\\'")
            val propertyType = marker.propertyType.replace("'", "\\'")

            webView.evaluateJavascript(
                "addPropertyMarker('${marker.id}', ${marker.latitude}, ${marker.longitude}, $price, '$currency', '$title', '$propertyType');",
                null
            )
        }
        // Fit bounds to show all markers
        if (markers.isNotEmpty()) {
            webView.evaluateJavascript("fitBounds();", null)
        }
    }

    // Update map layer type
    LaunchedEffect(mapLayerType) {
        val layerType = if (mapLayerType == MapLayerType.SATELLITE) "satellite" else "street"
        webView.evaluateJavascript("setMapLayer('$layerType');", null)
    }

    // Update drawing mode
    LaunchedEffect(isDrawingMode) {
        webView.evaluateJavascript("toggleDrawing($isDrawingMode);", null)
    }

    // Update 3D buildings
    LaunchedEffect(show3DBuildings) {
        webView.evaluateJavascript("toggle3DBuildings($show3DBuildings);", null)
    }

    // Update camera position
    LaunchedEffect(cameraLatitude, cameraLongitude, cameraZoom) {
        webView.evaluateJavascript(
            "setMapView($cameraLatitude, $cameraLongitude, ${cameraZoom.toInt()});",
            null
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            webView.destroy()
        }
    }

    AndroidView(
        factory = { webView },
        modifier = modifier
    )
}

class MapJavaScriptInterface(
    private val onMarkerClick: (String) -> Unit,
    private val onMapMoved: (Double, Double, Float) -> Unit,
    private val onAreaDrawn: (Double, Double, Double, Double) -> Unit
) {
    @JavascriptInterface
    fun onMarkerClick(propertyId: String) {
        onMarkerClick(propertyId)
    }

    @JavascriptInterface
    fun onMapMoved(lat: Double, lng: Double, zoom: Int) {
        onMapMoved(lat, lng, zoom.toFloat())
    }

    @JavascriptInterface
    fun onAreaDrawn(south: Double, west: Double, north: Double, east: Double) {
        onAreaDrawn(south, west, north, east)
    }
}
