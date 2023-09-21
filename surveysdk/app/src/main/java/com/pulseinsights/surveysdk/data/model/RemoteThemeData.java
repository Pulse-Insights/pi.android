package com.pulseinsights.surveysdk.data.model;

import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.data.model.base.FontStyleBase;
import com.pulseinsights.surveysdk.data.model.base.ImageStyleBase;

public class RemoteThemeData {
    public WidgetStyle widget = null;

    @SerializedName("close-button")
    public CloseBtnStyle closeBtn = null;

    @SerializedName("answer-button")
    public AnswerBtnStyle ansBtn = null;

    @SerializedName("radio-button")
    public RadioBtnStyle radio = null;

    @SerializedName("submit-button")
    public SubmitBtnStyle submitBtn = null;

    public CheckBoxStyle checkbox = null;
    public CheckMarkStyle checkmark = null;

    @SerializedName("font-large")
    public FontStyleBase largeFont = null;

    @SerializedName("font-medium")
    public FontStyleBase mediumFont = null;

    @SerializedName("font-small")
    public FontStyleBase smallFont = null;

    @SerializedName("free-text")
    public FreeTextStyle freeText = null;

    public BrandStyle branding = null;

    @SerializedName("invitation-text")
    public InviteTextStyle invite = null;

    @SerializedName("question-text")
    public QuestionTextStyle question = null;

    @SerializedName("answers-helper")
    public HelperStyle helper = null;

    @SerializedName("survey-image")
    public ImageStyleBase surveyImg = null;

    @SerializedName("response-image")
    public ImageStyleBase ansImg = null;

    @SerializedName("poll-bar")
    public PollBarStyle pollBar = null;
}
