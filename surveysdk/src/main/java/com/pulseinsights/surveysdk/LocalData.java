package com.pulseinsights.surveysdk;

import android.os.CountDownTimer;


import com.pulseinsights.surveysdk.data.model.PollResult;
import com.pulseinsights.surveysdk.data.model.Survey;
import com.pulseinsights.surveysdk.data.model.SurveyTicket;
import com.pulseinsights.surveysdk.define.ThemeStyles;
import com.pulseinsights.surveysdk.util.SurveyInlineResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LeoChao on 2016/10/18.
 */

public class LocalData {
    //Local Operate
    public String strUdid = "";
    public String strSubmitId = "";
    public String strAccountId = "";
    public String strCheckingSurveyId = "";
    public String strDeviceType = "native_mobile";
    public String strMobileType = "android";
    public int installDays = 0;
    public String strViewName = "";
    public String runningViewName = "";
    public boolean surveyWatcherEnable = true;
    public boolean isAppStarted = false;
    public boolean isSurveyApiRunning = false;
    public int surveyEventCode = 0;
    public int timerDurationInSecond = 1;
    public CountDownTimer mirrorTimer = null;
    public CountDownTimer delayTriggerTimer = null;
    public boolean previewMode = false;
    public boolean testModeEnable = false;
    public ThemeStyles themeStyles = new ThemeStyles();
    public Map<String, String> customData = new HashMap<>();
    public HashMap<String, SurveyInlineResult> inlineLink = new HashMap<>();
    //For Test only, but need to set back as false when release
    public Survey surveyPack = new Survey();
    public List<SurveyTicket> surveyTickets = new ArrayList<>();
    public List<PollResult> pollResults = new ArrayList<>();

    public static LocalData instant = new LocalData();
}
