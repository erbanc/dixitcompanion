# Stack traces lisibles dans les crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Room — garder les entités et DAOs
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-keep class fr.erban.dxitcompanion.db.** { *; }

# Gson — garder les classes sérialisées
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Parcelize — garder les implémentations Parcelable
-keepnames class * implements android.os.Parcelable
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# GraphView
-keep class com.jjoe64.graphview.** { *; }

# Kotlin
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-dontwarn kotlinx.**

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Jetpack Compose — géré automatiquement par AGP/R8, pas de règles manuelles nécessaires
