package com.android.miniuber

import android.os.Bundle
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.shared.miniuber.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // The app uses a light theme regardless of the device's system theme.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.BLACK),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.BLACK),
        )
        super.onCreate(savedInstanceState)

        val mapsConfigured = !packageManager.getApplicationInfo(
            packageName, PackageManager.GET_META_DATA
        ).metaData?.getString("com.google.android.geo.API_KEY").isNullOrBlank()

        setContent {
            App(mapsConfigured = mapsConfigured)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(mapsConfigured = false)
}
