package com.carly.vehiclemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.carly.vehiclemanager.navigation.NavigationRoot
import com.carly.vehicles.presentation.ui.theme.VehicleManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            // Keep splash screen until the app is ready
            // TODO: Replace with actual loading logic
            setKeepOnScreenCondition { false }
        }

        enableEdgeToEdge()

        setContent {
            VehicleManagerTheme {
                NavigationRoot()
            }
        }
    }
}