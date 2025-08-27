plugins {
    alias(libs.plugins.carly.android.feature.ui)
}

android {
    namespace = "com.carly.vehicles.presentation"
}

dependencies {
    implementation(projects.vehicles.domain)
    
    // Hilt Navigation Compose
    implementation(libs.androidx.hilt.navigation.compose)
    
    // Testing dependencies
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.arch.core.testing)
}