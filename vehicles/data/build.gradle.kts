plugins {
    alias(libs.plugins.carly.android.library)
    alias(libs.plugins.carly.android.room)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.carly.vehicles.data"
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)

    implementation(projects.vehicles.domain)
}