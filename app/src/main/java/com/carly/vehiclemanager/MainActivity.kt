package com.carly.vehiclemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.carly.vehiclemanager.navigation.NavigationRoot
import com.carly.vehicles.presentation.ui.theme.VehicleManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val splashViewModel: SplashViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        
        // Keep splash screen visible until seeding is complete
        splashScreen.setKeepOnScreenCondition {
            Thread.sleep(1) // Simulate a longer loading for demonstration
            splashViewModel.isLoading.value
        }

        // Start the seeding process
        lifecycleScope.launch {
            splashViewModel.initializeApp()
        }

        enableEdgeToEdge()

        setContent {
            VehicleManagerTheme {
                NavigationRoot()
            }
        }
    }
}