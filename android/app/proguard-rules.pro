# Proguard rules for QuickClock

# Keep Kotlin metadata
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }
-keepattributes *Annotation*

# Keep Jetpack Compose
-keep class androidx.compose.** { *; }
-keep class androidx.wear.compose.** { *; }

# Keep Data Store
-keep class androidx.datastore.** { *; }

# Keep our app classes
-keep class com.quickclock.app.** { *; }

# Keep ViewModel
-keep class androidx.lifecycle.ViewModel { *; }
-keep class * extends androidx.lifecycle.ViewModel { *; }

# Keep JSON
-keep class org.json.** { *; }

# Keep LocalDateTime
-keep class java.time.** { *; }
