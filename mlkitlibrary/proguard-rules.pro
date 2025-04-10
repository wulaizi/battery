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

-keep class com.extra.mlkitlibrary.BatteryTaskUtil {
    *;
}

-dontwarn com.drake.brv.PageRefreshLayout
-dontwarn com.drake.statelayout.StateLayout

# Keep Kotlin coroutines
-keep class kotlinx.coroutines.** { *; }
-keep class kotlinx.coroutines.internal.** { *; }

# Keep MLKit classes
-keep class com.google.mlkit.** { *; }
-keep class com.google.android.gms.** { *; }

# Keep Net library classes
-keep class com.drake.net.** { *; }

# Keep utility classes
-keep class com.blankj.utilcode.** { *; }

# Keep IPAddrUtils
-keep class com.extra.mlkitlibrary.utils.IPAddrUtils { *; }

# Keep StringConcatFactory
-keep class java.lang.invoke.StringConcatFactory { *; }

# Keep Kotlin metadata
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# Keep Kotlin coroutines
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Keep Kotlin suspend functions
-keepclassmembers class kotlinx.coroutines.** {
    kotlin.coroutines.Continuation *;
}

# Keep Kotlin coroutines internal
-keepclassmembers class kotlinx.coroutines.internal.** {
    kotlin.coroutines.Continuation *;
}

# Keep Kotlin coroutines debug
-keepclassmembers class kotlinx.coroutines.debug.** {
    kotlin.coroutines.Continuation *;
}

# Keep Kotlin coroutines flow
-keepclassmembers class kotlinx.coroutines.flow.** {
    kotlin.coroutines.Continuation *;
}

# Keep Kotlin coroutines channels
-keepclassmembers class kotlinx.coroutines.channels.** {
    kotlin.coroutines.Continuation *;
}

# Keep Kotlin coroutines select
-keepclassmembers class kotlinx.coroutines.selects.** {
    kotlin.coroutines.Continuation *;
}

# Keep Kotlin coroutines sync
-keepclassmembers class kotlinx.coroutines.sync.** {
    kotlin.coroutines.Continuation *;
}

# Keep Kotlin coroutines time
-keepclassmembers class kotlinx.coroutines.time.** {
    kotlin.coroutines.Continuation *;
}

# Keep Kotlin coroutines withContext
-keepclassmembers class kotlinx.coroutines.withContext.** {
    kotlin.coroutines.Continuation *;
}

