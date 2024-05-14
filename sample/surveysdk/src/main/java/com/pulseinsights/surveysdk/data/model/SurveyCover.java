package com.pulseinsights.surveysdk.data.model;

public class SurveyCover {
    public String id = "";
    public int surveyType = 0;
    public int widgetHeight = 0;
    public String invitation = "";
    public String invitationButton = "";
    public boolean invitationButtonDisable = false;
    public String thankYou = "";
    public String background = "";
    public boolean enablePendingStart = false;
    public int pendingStartTime = 0;
    public String inlineTrackId = "";

    public boolean displayAllQuestions = false;
    public boolean allAtOnceEmptyErrorEnabled = false;
    public String allAtOnceSubmitLabel = "";
    public String allAtOnceErrorText = "";
}
