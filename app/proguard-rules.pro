# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/anil/android-studio/sdk/tools/proguard/proguard-android.txt
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

-keep public class android.support.v7.widget.**{*;}
-keep public class android.support.v7.internal.widget.**{*;}
-keep public class android.support.v7.internal.view.menu.**{*;}
-keep public class * extends android.support.v4.view.MenuItemCompat {
      public <init>(android.content.Context);
}
-keep public class com.google.android.gms.ads.** {
public *;
}
-keep public class com.google.ads.** {
public *;
}
-dontwarn com.google.android.gms.**

-keepattributes SourceFile,LineNumberTable

-keep class com.sun.mail.handlers.** {*;}
-keep class javax.** {*;}
-keep class com.sun.** {*;}
-keep class myjava.** {*;}
-keep class org.apache.harmony.** {*;}
-keep public class Mail {*;}
-dontwarn java.awt.**
-dontwarn java.beans.Beans
-dontwarn javax.security.**

