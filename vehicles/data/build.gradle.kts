plugins {
    alias(libs.plugins.carly.android.library)
}

android {
    namespace = "com.carly.vehicles.data"
}

dependencies {
    implementation(projects.vehicles.domain)
}