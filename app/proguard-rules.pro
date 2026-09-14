# Xposed
-keep class de.robv.android.xposed.** { *; }
-keep class * implements de.robv.android.xposed.IXposedHookLoadPackage {
    <init>();
}
-keepclassmembers class * implements de.robv.android.xposed.IXposedHookLoadPackage {
    public void handleLoadPackage(de.robv.android.xposed.callbacks.XC_LoadPackage$LoadPackageParam);
}

# Module Class
-keep class com.popodyssey.rewardmax.** { *; }

# Jangan obfuscate method yang di-hook
-keepclassmembers class * {
    public int getAmount();
    public int getRewardAmount();
    public boolean isReady();
}