# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/drakeet/Applications/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#******************************************************************************

-dontwarn
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keepattributes *Annotation*
-keepattributes Signature
-keepattributes EnclosingMethod

# 保持混淆时类的实名及行号(——————— 调试时打开 ———————)
-keepattributes SourceFile,LineNumberTable


-keep public class * extends android.view.View {*;}
-keep public class * extends android.widget.BaseAdapter {*;}
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.**
-keep public class com.android.vending.licensing.ILicensingService


-keepclasseswithmembernames class * {
    native <methods>;
}

# 枚举需要keep see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    **[] $VALUES;
    public *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}

-keep public class [your_pkg].R$*{
    public static final int *;
}


-keep class android.support.** { *; }
-keep class com.viewpagerindicator.** {*;}
-keep class org.apache.commons.httpclient.** {*;}
-keep class com.loopj.android.** {*;}
-keep class org.android.agoo.** {*;}
-keep class com.tencent.** {*;}
-keep class com.nostra13.universalimageloader.** {*;}
-keep class com.sina.** {*;}
-keep class com.facebook.** {*;}


-dontwarn android.support.**
-dontwarn com.viewpagerindicator.**
-dontwarn org.apache.commons.httpclient.**
-dontwarn com.loopj.android.**
-dontwarn org.android.agoo.**
-dontwarn com.tencent.**
-dontwarn com.nostra13.universalimageloader.**
-dontwarn com.sina.**
-dontwarn com.facebook.**



#******************************************************************************

#okhttp

-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**


-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }
-dontwarn butterknife.internal.**

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}


-keep public class com.xilu.wybz.R$*{
		public static final int *;
}


# Keep the support library
-keep class android.support.** { *; }
-keep interface android.support.** { *; }


# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.idea.fifaalarmclock.entity.***
-dontwarn java.lang.invoke.*


-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}


-keep class android.support.v8.renderscript.** { *; }


# EventBus
-keepclasseswithmembers class de.greenrobot.event.** {*;}
-keepclasseswithmembers class com.xilu.wybz.** {*;}
-keepclassmembers class ** {
    public void onEvent*(**);
}
-dontwarn de.greenrobot.event.**


# For RxJava:
-dontwarn org.mockito.**
-dontwarn org.junit.**
-dontwarn org.robolectric.**

-dontwarn rx.**
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

# umeng

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep public class * extends com.umeng.**
-keep class com.umeng.** {*; }

-keep public class com.idea.fifaalarmclock.app.R$*{
    public static final int *;
}

-keep public class com.umeng.fb.ui.ThreadView {
}

-dontwarn com.umeng.**
-dontwarn org.apache.commons.**



#log
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int d(...);
    public static int w(...);
    public static int v(...);
    public static int i(...);
}