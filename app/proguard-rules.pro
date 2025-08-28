# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ApplicationComponentManager { *; }
-keep class * extends dagger.hilt.android.internal.managers.ActivityComponentManager { *; }

# Room Database
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }
-keep class * extends androidx.room.migration.Migration { *; }

# Keep all DAOs and entities
-keep class com.carly.vehicles.data.database.dao.** { *; }
-keep class com.carly.vehicles.data.database.entity.** { *; }
-keep class com.carly.vehicles.data.database.** { *; }

# Keep data sources and repositories
-keep class com.carly.vehicles.data.datasource.** { *; }
-keep class com.carly.vehicles.data.repository.** { *; }

# Keep Hilt modules and generated classes
-keep class com.carly.vehicles.data.di.** { *; }
-keep class com.carly.vehicles.presentation.di.** { *; }
-keep class **_HiltModules { *; }
-keep class **_HiltComponents { *; }
-keep class **_Factory { *; }
-keep class **_MembersInjector { *; }

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory { }
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler { }
-keep class kotlinx.coroutines.** { *; }

# DataStore
-keep class androidx.datastore.** { *; }
-keep class com.google.protobuf.** { *; }

# Keep domain models
-keep class com.carly.vehicles.domain.model.** { *; }

# Jetpack Compose
-keep class androidx.compose.** { *; }
-keep class androidx.activity.ComponentActivity { *; }
-keep class androidx.lifecycle.** { *; }