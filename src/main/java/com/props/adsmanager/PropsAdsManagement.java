package com.props.adsmanager;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.props.adsmanager.Models.PropsAdsManagementModels;
import com.props.adsmanager.connection.API;
import com.props.adsmanager.connection.PROPS_REST_API;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropsAdsManagement extends LinearLayout {
    private LinearLayout ads_linearlayout;
    private AdView adView;
    private Context context;
    private String adUnitId = "";
    private String targetedAdUnit;
    public static Map<String, String> adsMapping  = new HashMap<>();
    public static InterstitialAd mInterstitialAd;
    public static RewardedAd mRewardedAd;
    private static boolean isMappingInitialized = false;

    public static void initializeAdmob(Context context) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });
    }

    private static void requestAdunitData(String apkName, Context context) {
        API api = PROPS_REST_API.createAPI();
        Call<List<PropsAdsManagementModels>> cb = api.getAdsPosition(apkName);

        cb.enqueue(new Callback<List<PropsAdsManagementModels>>() {
            @Override
            public void onResponse(Call<List<PropsAdsManagementModels>> call, Response<List<PropsAdsManagementModels>> response) {
                if (response.isSuccessful()) {
                    List<PropsAdsManagementModels> data = response.body();

                    for (PropsAdsManagementModels model : data) {
                        String pos = "";

                        if (model.type.equals("native")) {
                            pos = "native_1";
                        } else if (model.type.equals("interstitial")) {
                            pos = "interstitial_1";
                        } else if (model.type.equals("rewarded")) {
                            pos = "rewarded_1";
                        } else if (model.type.equals("openapp")) {
                            pos = "openapp_1";
                        } else {
                            pos = "banner_1";
                        }
                        SharedPreferences shared_ads_1 = context.getSharedPreferences(pos, Context.MODE_PRIVATE);
                        PropsAdsManagement.setSharedpref(shared_ads_1, model.adUnitID, model.position);
                        PropsAdsManagement.adsMapping.put(model.position, model.adUnitID);
                    }
                    PropsAdsManagement.isMappingInitialized = true;
                }
            }

            @Override
            public void onFailure(Call<List<PropsAdsManagementModels>> call, Throwable t) {

            }
        });
    }

    public static void setSharedpref (SharedPreferences pref, String value, String alias) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("id", value);
        editor.putString("alias", alias);
        editor.apply();
    }

    public static void loadRewardedAds(Context context, String mapping, RewardedAdLoadCallback cb) {
        AdRequest adRequest = new AdRequest.Builder().build();
        String getMapping = PropsAdsManagement.adsMapping.get(mapping);
        if (getMapping == null || getMapping == "") {
            getMapping = "";
        }
        RewardedAd.load(context, getMapping, adRequest,
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd  rewardedAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        PropsAdsManagement.mRewardedAd = rewardedAd;
                        cb.onAdLoaded(rewardedAd);
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        PropsAdsManagement.mRewardedAd = null;
                        cb.onAdFailedToLoad(loadAdError);
                    }
                });
    }
    public static void triggerRewardedAds(Activity activity, OnUserEarnedRewardListener func) {
        if (PropsAdsManagement.mRewardedAd != null) {
            PropsAdsManagement.mRewardedAd.show(activity, func);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }


    public static void loadInterstitialAds(Context context, String mapping, InterstitialAdLoadCallback loadCallback) {
        AdRequest adRequest = new AdRequest.Builder().build();
        String getMapping = PropsAdsManagement.adsMapping.get(mapping);
        if (getMapping == null || getMapping == "") {
            getMapping = "";
        }
        InterstitialAd.load(context, getMapping, adRequest,
            new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    PropsAdsManagement.mInterstitialAd = interstitialAd;
                    loadCallback.onAdLoaded(interstitialAd);
                    Log.i(TAG, "onAdLoaded");
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    Log.d(TAG, loadAdError.toString());
                    PropsAdsManagement.mInterstitialAd = null;
                    loadCallback.onAdFailedToLoad(loadAdError);
                }
            });
    }

    public static InterstitialAd getInterstitialAds() {
        return mInterstitialAd;
    }

    public static RewardedAd getRewardedAds() {
        return mRewardedAd;
    }

    public static void triggerInterstitialAds(Activity activity) {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(activity);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    public static String getAdsIdFromPref (SharedPreferences pref) {
        String val = pref.getString("id", "");

        return val;
    }

    public static String getAliasFromPref (SharedPreferences pref) {
        String val = pref.getString("alias", "");
        return val;
    }

    public static String getOpenAppAdsId(String mapping) {
        String getMapping = PropsAdsManagement.adsMapping.get(mapping);
        if (getMapping == null || getMapping == "") {
            getMapping = "";
        }
        return getMapping;
    }

    public static String getNativeAdsId(String mapping) {
        String getMapping = PropsAdsManagement.adsMapping.get(mapping);
        if (getMapping == null || getMapping == "") {
            getMapping = "";
        }
        return getMapping;
    }



    public static void initializeAdsMapping(Context context) {
        SharedPreferences shared_ads_1 = context.getSharedPreferences("banner_1", Context.MODE_PRIVATE);
        SharedPreferences shared_interstitial_1 = context.getSharedPreferences("interstitial_1", Context.MODE_PRIVATE);
        SharedPreferences shared_openapp_1 = context.getSharedPreferences("openapp_1", Context.MODE_PRIVATE);
        SharedPreferences shared_native_1 = context.getSharedPreferences("native_1", Context.MODE_PRIVATE);
        SharedPreferences shared_rewarded_1 = context.getSharedPreferences("rewarded_1", Context.MODE_PRIVATE);

        String banner_1 = getAdsIdFromPref(shared_ads_1);
        String interstitial_1 = getAdsIdFromPref(shared_interstitial_1);
        String openapp_1 = getAdsIdFromPref(shared_openapp_1);
        String native_1 = getAdsIdFromPref(shared_native_1);
        String rewarded_1 = getAdsIdFromPref(shared_rewarded_1);

        String alias_banner_1 = getAliasFromPref(shared_ads_1);
        String alias_interstitial_1 = getAliasFromPref(shared_interstitial_1);
        String alias_openapp_1 = getAliasFromPref(shared_openapp_1);
        String alias_native_1 = getAliasFromPref(shared_native_1);
        String alias_rewarded_1 = getAliasFromPref(shared_rewarded_1);

        PropsAdsManagement.adsMapping.put(alias_banner_1, banner_1);
        PropsAdsManagement.adsMapping.put(alias_interstitial_1, interstitial_1);
        PropsAdsManagement.adsMapping.put(alias_openapp_1, openapp_1);
        PropsAdsManagement.adsMapping.put(alias_native_1, native_1);
        PropsAdsManagement.adsMapping.put(alias_rewarded_1, rewarded_1);

        PropsAdsManagement.requestAdunitData(context.getPackageName(), context);


        for(Map.Entry<String, String> entry : adsMapping.entrySet()) {
            System.out.println("Alienads Key Admap: " + entry.getKey() + " | Ad Unit ID: " + entry.getValue());
        }
    }

    public PropsAdsManagement(Context context) {
        super(context);
        this.context = context;
        initializeAdsManagement(context);
    }

    public PropsAdsManagement(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeAdsManagement(context);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PropsAdsManagement);
        try {
            String getSizes = ta.getString(R.styleable.PropsAdsManagement_adSize);
            String getPositionTargeting = ta.getString(R.styleable.PropsAdsManagement_positionTargeting);

            this.createBanner(getSizes, getPositionTargeting);
        } finally {
            ta.recycle();
        }
        initializeAdsManagement(context);
    }

    private void initializeAdsManagement(Context context) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.props_ads_management, this);

        this.ads_linearlayout = findViewById(R.id.ads_linearlayout);

    }

    public AdView createBannerAdview(String sizes, String mapping) {
        AdSize adSize = null;

        if (sizes.equals("BANNER")) {
            adSize = AdSize.BANNER;
        } else if (sizes.equals("LARGE_BANNER")) {
            adSize = AdSize.LARGE_BANNER;
        }  else if (sizes.equals("MEDIUM_RECTANGLE")) {
            adSize = AdSize.MEDIUM_RECTANGLE;
        } else if (sizes.equals("FULL_BANNER")) {
            adSize = AdSize.FULL_BANNER;
        } else if (sizes.equals("LEADERBOARD")){
            adSize = AdSize.LEADERBOARD;
        } else {
            adSize = AdSize.BANNER;
        }

        adView = new AdView(this.context);
        String getMapping = PropsAdsManagement.adsMapping.get(mapping);
        if (getMapping == null) {
            getMapping = this.adUnitId;
        }
        adView.setAdSize(adSize);
        adView.setAdUnitId(getMapping);
        adView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return adView;
    }

    public LinearLayout createBanner(String sizes, String mapping) {
        AdSize adSize = null;

        if (sizes.equals("BANNER")) {
            adSize = AdSize.BANNER;
        } else if (sizes.equals("LARGE_BANNER")) {
            adSize = AdSize.LARGE_BANNER;
        }  else if (sizes.equals("MEDIUM_RECTANGLE")) {
            adSize = AdSize.MEDIUM_RECTANGLE;
        } else if (sizes.equals("FULL_BANNER")) {
            adSize = AdSize.FULL_BANNER;
        } else if (sizes.equals("LEADERBOARD")){
            adSize = AdSize.LEADERBOARD;
        } else {
            adSize = AdSize.BANNER;
        }

        adView = new AdView(this.context);
        String getMapping = PropsAdsManagement.adsMapping.get(mapping);
        if (getMapping == null) {
            getMapping = this.adUnitId;
        }
        adView.setAdSize(adSize);
        adView.setAdUnitId(getMapping);
        adView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.ads_linearlayout.addView(adView);

        AdRequest adrequest = new AdRequest.Builder().build();
        adView.loadAd(adrequest);
        return this.ads_linearlayout;
    }

    public LinearLayout createBanner(AdSize adSize, String mapping) {
        adView = new AdView(this.context);
        String getMapping = PropsAdsManagement.adsMapping.get(mapping);
        if (getMapping == null) {
            getMapping = this.adUnitId;
        }
        adView.setAdUnitId(adUnitId);
        adView.setAdSize(adSize);
        this.ads_linearlayout.addView(adView);
        return this.ads_linearlayout;
    }

    public LinearLayout createCustomSizedBanner(int width, int height, String mapping) {
        AdSize adSize = new AdSize(width, height);
        adView = new AdView(this.context);
        String getMapping = PropsAdsManagement.adsMapping.get(mapping);
        if (getMapping == null) {
            getMapping = "";
        }
        adView.setAdUnitId(getMapping);
        this.ads_linearlayout.addView(adView);
        return this.ads_linearlayout;
    }

    public AdView createCustomSizedBannerAdview(int width, int height, String mapping) {
        AdSize adSize = new AdSize(width, height);
        adView = new AdView(this.context);
        String getMapping = PropsAdsManagement.adsMapping.get(mapping);
        if (getMapping == null) {
            getMapping = "";
        }

        adView.setAdSize(adSize);
        adView.setAdUnitId(getMapping);
        adView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return adView;
    }
}
