package com.popodyssey.rewardmax;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class RewardMaxModule implements IXposedHookLoadPackage {
    
    private static final String TAG = "PopOdysseyRewardMax";
    private static final int MAX_REWARD_AMOUNT = 999999;
    private static final String MAX_REWARD_NAME = "MAX_REWARD";
    
    // Daftar semua package game yang akan di-hook
    private static final Set<String> TARGET_PACKAGES = new HashSet<>(Arrays.asList(
        "com.terraform.popodyssey",
        "com.bubble.crush.stick.magicgames",
        "org.beastcoast.major.tilegame.candy",
        "com.spiritbreaker.games.crashycars",
        "com.shadowgames.dev.crystal.blast",
        "com.magicgames.dessert.crush.stick",
        "com.funnyvideo.cgame.fruitblast",
        "com.fungames2d.furry.pop",
        "com.gem.mine.stick.magicgames",
        "com.spiritbreaker.games.tilejuicy",
        "com.omndev.tapgame.kittymatch",
        "com.mine.sweeper.magicstick.games",
        "com.antiboom.puzzle.monster.clear.game",
        "com.sheep.music.stack",
        "com.omndev.mqgame.globeblast",
        "com.match3.game.patch.panic.terraform",
        "com.fungames2d.tastytile",
        "com.fungames2d.tile.cat",
        "com.funnyvideo.cgame.treasure.elimination",
        "com.shadowgames.dev.universe.match",
        "com.funnyvideo.cgame.vitamin.pop",
        "com.sheep.word.tile"
    ));
    
    // Caching untuk mencegah hook berulang
    private static final Set<String> hookedPackages = ConcurrentHashMap.newKeySet();
    private static final Map<String, Class<?>> classCache = new ConcurrentHashMap<>();
    
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // Cek apakah package yang dimuat ada dalam daftar target
        if (!TARGET_PACKAGES.contains(lpparam.packageName)) {
            return;
        }
        
        // Cegah hook berulang pada package yang sama
        if (hookedPackages.contains(lpparam.packageName)) {
            XposedBridge.log("[" + TAG + "] Already hooked: " + lpparam.packageName);
            return;
        }
        hookedPackages.add(lpparam.packageName);
        
        XposedBridge.log("[" + TAG + "] Module loaded for: " + lpparam.packageName);
        
        // Hook semua SDK dengan error handling
        hookAppLovinMax(lpparam);
        hookAlexMaxAdapter(lpparam);
        hookAnyThinkSDK(lpparam);
        hookPangleSDK(lpparam);
        hookVungleSDK(lpparam);
        hookInMobiSDK(lpparam);
        hookMBridgeSDK(lpparam);
        hookBigoAdsSDK(lpparam);
        hookFyberSDK(lpparam);
        hookUnityAds(lpparam);
        hookIronSource(lpparam);
        hookAdMob(lpparam);
        hookChartboost(lpparam);
        hookFacebookAudience(lpparam);
        
        XposedBridge.log("[" + TAG + "] Hooking completed for: " + lpparam.packageName);
    }
    
    // Helper method untuk caching class lookup
    private Class<?> findCachedClass(XC_LoadPackage.LoadPackageParam lpparam, String className) {
        String cacheKey = lpparam.packageName + ":" + className;
        Class<?> cached = classCache.get(cacheKey);
        if (cached != null) return cached;
        
        Class<?> clazz = XposedHelpers.findClassIfExists(className, lpparam.classLoader);
        if (clazz != null) {
            classCache.put(cacheKey, clazz);
        }
        return clazz;
    }
    
    private void hookAppLovinMax(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> maxRewardClass = findCachedClass(lpparam, "com.applovin.mediation.MaxReward");
            
            if (maxRewardClass != null) {
                XposedHelpers.findAndHookMethod(maxRewardClass, "getAmount", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(MAX_REWARD_AMOUNT);
                    }
                });
                XposedBridge.log("[" + TAG + "] AppLovin MaxReward hooked");
            }
            
            Class<?> maxRewardedAdClass = findCachedClass(lpparam, "com.applovin.mediation.ads.MaxRewardedAd");
            
            if (maxRewardedAdClass != null) {
                XposedHelpers.findAndHookMethod(maxRewardedAdClass, "isReady", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(true);
                    }
                });
            }
        } catch (Exception e) {
            XposedBridge.log("[" + TAG + "] AppLovin Error: " + e.getMessage());
        }
    }
    
    private void hookAlexMaxAdapter(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> alexRewardAd = findCachedClass(lpparam, "com.alex.AlexMaxRewardAd");
            
            if (alexRewardAd != null) {
                XposedHelpers.findAndHookMethod(alexRewardAd, "getRewardAmount", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(MAX_REWARD_AMOUNT);
                    }
                });
                XposedBridge.log("[" + TAG + "] Alex SDK hooked");
            }
        } catch (Exception e) {
            XposedBridge.log("[" + TAG + "] Alex Error: " + e.getMessage());
        }
    }
    
    private void hookAnyThinkSDK(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> atAdInfo = findCachedClass(lpparam, "com.anythink.core.api.ATAdInfo");
            
            if (atAdInfo != null) {
                XposedHelpers.findAndHookMethod(atAdInfo, "getRewardAmount", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(MAX_REWARD_AMOUNT);
                    }
                });
                XposedBridge.log("[" + TAG + "] AnyThink SDK hooked");
            }
        } catch (Exception e) {
            XposedBridge.log("[" + TAG + "] AnyThink Error: " + e.getMessage());
        }
    }
    
    private void hookPangleSDK(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> pagRewardItem = findCachedClass(lpparam, "com.bytedance.sdk.openadsdk.api.reward.PAGRewardItem");
            
            if (pagRewardItem != null) {
                XposedHelpers.findAndHookMethod(pagRewardItem, "getRewardAmount", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(MAX_REWARD_AMOUNT);
                    }
                });
                XposedBridge.log("[" + TAG + "] Pangle SDK hooked");
            }
        } catch (Exception e) {
            XposedBridge.log("[" + TAG + "] Pangle Error: " + e.getMessage());
        }
    }
    
    private void hookVungleSDK(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> vungleReward = findCachedClass(lpparam, "com.vungle.ads.VungleReward");
            
            if (vungleReward != null) {
                XposedHelpers.findAndHookMethod(vungleReward, "getRewardAmount", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(MAX_REWARD_AMOUNT);
                    }
                });
                XposedBridge.log("[" + TAG + "] Vungle SDK hooked");
            }
        } catch (Exception e) {
            XposedBridge.log("[" + TAG + "] Vungle Error: " + e.getMessage());
        }
    }
    
    private void hookInMobiSDK(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> inMobiReward = findCachedClass(lpparam, "com.inmobi.ads.InMobiReward");
            
            if (inMobiReward != null) {
                XposedHelpers.findAndHookMethod(inMobiReward, "getRewardAmount", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(MAX_REWARD_AMOUNT);
                    }
                });
                XposedBridge.log("[" + TAG + "] InMobi SDK hooked");
            }
        } catch (Exception e) {
            XposedBridge.log("[" + TAG + "] InMobi Error: " + e.getMessage());
        }
    }
    
    private void hookMBridgeSDK(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> mbRewardInfo = findCachedClass(lpparam, "com.mbridge.msdk.out.RewardInfo");
            
            if (mbRewardInfo != null) {
                XposedHelpers.findAndHookMethod(mbRewardInfo, "getRewardAmount", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(MAX_REWARD_AMOUNT);
                    }
                });
                XposedBridge.log("[" + TAG + "] MBridge SDK hooked");
            }
        } catch (Exception e) {
            XposedBridge.log("[" + TAG + "] MBridge Error: " + e.getMessage());
        }
    }
    
    private void hookBigoAdsSDK(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> bigoReward = findCachedClass(lpparam, "sg.bigo.ads.reward.RewardItem");
            
            if (bigoReward != null) {
                XposedHelpers.findAndHookMethod(bigoReward, "getRewardAmount", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(MAX_REWARD_AMOUNT);
                    }
                });
                XposedBridge.log("[" + TAG + "] Bigo SDK hooked");
            }
        } catch (Exception e) {
            XposedBridge.log("[" + TAG + "] Bigo Error: " + e.getMessage());
        }
    }
    
    private void hookFyberSDK(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> fyberReward = findCachedClass(lpparam, "com.fyber.inneractive.sdk.external.InneractiveRewardedVideoAd");
            
            if (fyberReward != null) {
                XposedHelpers.findAndHookMethod(fyberReward, "getRewardAmount", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(MAX_REWARD_AMOUNT);
                    }
                });
                XposedBridge.log("[" + TAG + "] Fyber SDK hooked");
            }
        } catch (Exception e) {
            XposedBridge.log("[" + TAG + "] Fyber Error: " + e.getMessage());
        }
    }
    
    private void hookUnityAds(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> unityReward = findCachedClass(lpparam, "com.unity3d.services.core.api.Reward");
            
            if (unityReward != null) {
                XposedHelpers.findAndHookMethod(unityReward, "getRewardAmount", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(MAX_REWARD_AMOUNT);
                    }
                });
                XposedBridge.log("[" + TAG + "] Unity Ads hooked");
            }
        } catch (Exception e) {
            XposedBridge.log("[" + TAG + "] Unity Error: " + e.getMessage());
        }
    }
    
    private void hookIronSource(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> ironReward = findCachedClass(lpparam, "com.ironsource.mediationsdk.model.Placement");
            
            if (ironReward != null) {
                XposedHelpers.findAndHookMethod(ironReward, "getRewardAmount", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(MAX_REWARD_AMOUNT);
                    }
                });
                XposedBridge.log("[" + TAG + "] IronSource hooked");
            }
        } catch (Exception e) {
            XposedBridge.log("[" + TAG + "] IronSource Error: " + e.getMessage());
        }
    }
    
    // Tambahan: AdMob/Google Mobile Ads
    private void hookAdMob(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> admobReward = findCachedClass(lpparam, "com.google.android.gms.ads.rewarded.RewardItem");
            
            if (admobReward != null) {
                XposedHelpers.findAndHookMethod(admobReward, "getAmount", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(MAX_REWARD_AMOUNT);
                    }
                });
                XposedBridge.log("[" + TAG + "] AdMob hooked");
            }
        } catch (Exception e) {
            XposedBridge.log("[" + TAG + "] AdMob Error: " + e.getMessage());
        }
    }
    
    // Tambahan: Chartboost
    private void hookChartboost(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> cbReward = findCachedClass(lpparam, "com.chartboost.sdk.ads.RewardedReward");
            
            if (cbReward != null) {
                XposedHelpers.findAndHookMethod(cbReward, "getRewardAmount", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(MAX_REWARD_AMOUNT);
                    }
                });
                XposedBridge.log("[" + TAG + "] Chartboost hooked");
            }
        } catch (Exception e) {
            XposedBridge.log("[" + TAG + "] Chartboost Error: " + e.getMessage());
        }
    }
    
    // Tambahan: Facebook Audience Network
    private void hookFacebookAudience(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> fbReward = findCachedClass(lpparam, "com.facebook.ads.RewardedVideoAd");
            
            if (fbReward != null) {
                XposedHelpers.findAndHookMethod(fbReward, "getRewardAmount", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(MAX_REWARD_AMOUNT);
                    }
                });
                XposedBridge.log("[" + TAG + "] Facebook Audience hooked");
            }
        } catch (Exception e) {
            XposedBridge.log("[" + TAG + "] Facebook Error: " + e.getMessage());
        }
    }
}