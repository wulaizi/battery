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

# 混淆所有类成员，但保留公共API接口
#-keep public class com.extra.mlkitlibrary.manager.HttpManager {
#    public <methods>;
#}

-keep public class com.extra.mlkitlibrary.manager.MlKitManager{
   *;
}

#-keep public class com.extra.mlkitlibrary.utils.AESUtil {
#    public <methods>;
#}
#
#-keep public class com.extra.mlkitlibrary.utils.BlowfishUtil {
#    public <methods>;
#}

#-keep public class com.extra.mlkitlibrary.kt.LogKt {
#    public <methods>;
#}

## 混淆所有其他类和方法
#-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
#-dontoptimize
#-dontpreverify
