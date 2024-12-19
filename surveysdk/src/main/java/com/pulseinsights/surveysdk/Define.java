package com.pulseinsights.surveysdk;

/**
 * Created by LeoChao on 2016/10/11.
 */

public class Define {
    //Config part
    //public String strPISurvetSite = "http://survey.pulseinsights.com/";
    public static String SURVEY_REQ_TYPE_SCAN = "0";
    public static String SURVEY_REQ_TYPE_PRESENT = "1";
    public static String SURVEY_REQ_TYPE_GETCONTENT = "2";
    public static String SURVEY_REQ_TYPE_SENDANSWER = "3";
    public static String SURVEY_REQ_TYPE_CLOSE = "4";

    public static String SURVEY_REQ_VIEWED_AT = "5";

    public static int PULSE_INSIGHTS_EVENT_CODE_NORMAL = 0;
    public static int PULSE_INSIGHTS_EVENT_CODE_SURVEY_JUST_CLOSED = 1;
    public static int PULSE_INSIGHTS_EVENT_CODE_ACCOUNT_RESETED = 2;
}

