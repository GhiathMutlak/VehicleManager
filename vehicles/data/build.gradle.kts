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
    
    // Testing dependencies
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.arch.core.testing)
}