plugins {
    alias(libs.plugins.carly.android.feature.ui)
}

android {
    namespace = "com.carly.vehicles.presentation"

    packaging {
        resources {
            // Exclude or pick first occurrence of conflicting files
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.txt"
        }
    }
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
    testImplementation(libs.hilt.android.testing)
    kspTest(libs.hilt.compiler)
    
    // Android UI Testing dependencies
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(projects.app)
    kspAndroidTest(libs.hilt.compiler)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}