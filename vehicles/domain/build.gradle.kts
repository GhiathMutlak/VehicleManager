plugins {
    alias(libs.plugins.carly.jvm.library)
}

dependencies {
    implementation(libs.coroutines.core)
    
    // Test dependencies
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
}
