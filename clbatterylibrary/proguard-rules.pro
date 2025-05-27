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

-keep class com.extra.clbatterylibrary.BatteryTaskUtil {
    *;
}
# 将所有其他混淆类统一放入 x.battery 包，避免与其他 AAR 冲突
-repackageclasses x.battery

-keep class com.google.mlkit. { *; }
-keep class com.google.android.gms.tasks. { *; }

# 或者直接使用 -dontwarn 忽略警告（不太推荐）
# -dontwarn com.google.mlkit.
# -dontwarn com.google.android.gms.tasks.

-dontwarn com.drake.brv.PageRefreshLayout
-dontwarn com.drake.statelayout.StateLayout

