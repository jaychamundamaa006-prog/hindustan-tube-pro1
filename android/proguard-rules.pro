# Hindustan Tube Pro — Proguard rules

# Keep TWA / browser helper entry points
-keep class com.google.androidbrowser.** { *; }
-keep class androidx.browser.customtabs.** { *; }

# Standard Android optimisation
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions
-keepattributes InnerClasses

# Strip Log calls in release
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}
