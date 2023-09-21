package com.pulseinsights.surveysdk.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hyeonmin on 3/24/15.
 * Remade by LeoChao on 10/20/16.
 */
public class PreferencesManager {

    private static PreferencesManager instance;

    final Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static final String PI_DEVICE_UDID = "DEVICE_UDID";
    public static final String PI_IS_DEBUG_MODE_ENABLE = "IS_DEBUG_MODE_ENABLE";
    public static final String PI_SERVER_HOST = "SERVER_HOST";
    public static final String PI_LAUNCH_COUNT = "LAUNCH_COUNT";
    public static final String PI_SURVEY_HISTORY = "SURVEY_HISTORY";
    public static final String PI_CLIENT_KEY = "CLIENT_KEY";

    private boolean isDebugModeOn;
    private String strServerHostUrl = "";
    private int launchCount;
    private String strClientKey = "";


    //Constructors

    public static PreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context);
        }
        return instance;
    }

    private PreferencesManager(Context context) {
        this.context = context;
        sharedPreferences = this.context
                .getSharedPreferences("PulseInsights", Context.MODE_PRIVATE);
        editor = this.context
                .getSharedPreferences("PulseInsights", Context.MODE_PRIVATE).edit();
        isDebugModeOn = sharedPreferences.getBoolean(PI_IS_DEBUG_MODE_ENABLE, false);
        strServerHostUrl = sharedPreferences
                .getString(PI_SERVER_HOST, "https://survey.pulseinsights.com");
        launchCount = sharedPreferences.getInt(PI_LAUNCH_COUNT, 0);
        strClientKey = sharedPreferences.getString(PI_CLIENT_KEY, "");
    }

    public void setClientKey(String clientKey) {
        strClientKey = clientKey;
        editor.putString(PI_CLIENT_KEY, clientKey);
        editor.apply();
    }

    public String getClientKey() {
        return strClientKey;
    }

    public boolean isSurveyAnswered(String surveyId) {
        boolean rtRes = false;
        Set<String> set = new HashSet<String>();
        set = sharedPreferences.getStringSet(PI_SURVEY_HISTORY, new HashSet<String>());
        rtRes = set.contains(surveyId);

        return rtRes;
    }

    public void logAnsweredSurvey(String surveyId) {
        Set<String> set = new HashSet<String>();
        set = sharedPreferences.getStringSet(PI_SURVEY_HISTORY, new HashSet<String>());
        set.add(surveyId);
        editor.putStringSet(PI_SURVEY_HISTORY, set);
        editor.apply();
    }

    public void changeHostUrl(String strSetHost) {
        strServerHostUrl = strSetHost;
        editor.putString(PI_SERVER_HOST, strSetHost);
        editor.apply();
    }

    public int getLaunchCount() {
        return launchCount;
    }

    public void addLaunchCount() {
        launchCount++;
        editor.putInt(PI_LAUNCH_COUNT, launchCount);
        editor.apply();
    }

    public String getServerHost() {
        return strServerHostUrl;
    }

    public void changeDebugModeSetting(boolean modeEnable) {
        isDebugModeOn = modeEnable;
        editor.putBoolean(PI_IS_DEBUG_MODE_ENABLE, modeEnable);
        editor.apply();
    }

    public boolean isDebugModeEnable() {
        return isDebugModeOn;
    }

    public void saveDeviceUdid(String strDevUdid) {
        editor.putString(PI_DEVICE_UDID, strDevUdid);
        editor.apply();
    }

    public String getDeviceUdid() {
        return sharedPreferences.getString(PI_DEVICE_UDID, "");
    }

    public void resetDeviceUdid() {
        editor.putString(PI_DEVICE_UDID, "");
        editor.putStringSet(PI_SURVEY_HISTORY, new HashSet<String>());
        editor.apply();
    }

}
