plugins {
    alias(libs.plugins.carly.android.feature.ui)
}

android {
    namespace = "com.carly.vehicles.presentation"
}

dependencies {
    implementation(projects.vehicles.domain)
}