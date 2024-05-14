package com.pulseinsights.surveysdk.util;

import android.content.Context;
import android.util.Log;

/**
 * Created by LeoChao on 2016/10/20.
 */

public class DebugTool {
    public static void debugPrintln(Context content, String strPrintItem) {
        if (PreferencesManager.getInstance(content).isDebugModeEnable()) {
            Log.i("PI/DebugMsg", strPrintItem);
        }
    }

    public static void debugPrintln(Context content, String strTag, String strPrintItem) {
        if (PreferencesManager.getInstance(content).isDebugModeEnable()) {
            Log.i(strTag, strPrintItem);
        }
    }
}
