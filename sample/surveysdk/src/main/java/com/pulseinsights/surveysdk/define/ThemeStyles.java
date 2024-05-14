package com.pulseinsights.surveysdk.define;

import com.pulseinsights.surveysdk.data.model.RemoteThemeData;

public class ThemeStyles {
    public WidgetTheme widget = new WidgetTheme();
    public CloseBtnTheme closeBtn = new CloseBtnTheme();
    public AnswerBtnTheme ansBtn = new AnswerBtnTheme();
    public RadioBtnTheme radio = new RadioBtnTheme();
    public SubmitBtnTheme submitBtn = new SubmitBtnTheme();
    public CheckboxTheme checkBox = new CheckboxTheme();
    public CheckMarkTheme checkMark = new CheckMarkTheme();
    public FontLargeTheme largeFont = new FontLargeTheme();
    public FontMediumTheme mediumFont = new FontMediumTheme();
    public FontSmallTheme smallFont = new FontSmallTheme();
    public FreeTextTheme freeText = new FreeTextTheme();
    public BrandTheme brand = new BrandTheme();
    public InviteTextTheme invite = new InviteTextTheme();
    public QuestionTextTheme question = new QuestionTextTheme();
    public QuestionErrorTextTheme questionError = new QuestionErrorTextTheme();
    public SurveyImageTheme surveyImg = new SurveyImageTheme();
    public ResponseImageTheme ansImg = new ResponseImageTheme();
    public PollBarTheme pollBar = new PollBarTheme();
    public HelperTheme helper = new HelperTheme();

    public void updateAssignTheme(RemoteThemeData fetchStyle) {
        widget.applyNewStyle(
                fetchStyle.widget != null ? fetchStyle.widget.toTheme() : new WidgetTheme());
        closeBtn.applyNewStyle(
                fetchStyle.closeBtn != null ? fetchStyle.closeBtn.toTheme() : new CloseBtnTheme());
        ansBtn.applyNewStyle(
                fetchStyle.ansBtn != null ? fetchStyle.ansBtn.toTheme() : new AnswerBtnTheme());
        radio.applyNewStyle(
                fetchStyle.radio != null ? fetchStyle.radio.toTheme() : new RadioBtnTheme());
        submitBtn.applyNewStyle(fetchStyle.submitBtn != null
                ? fetchStyle.submitBtn.toTheme() : new SubmitBtnTheme());
        checkBox.applyNewStyle(
                fetchStyle.checkbox != null ? fetchStyle.checkbox.toTheme() : new CheckboxTheme());
        checkMark.applyNewStyle(fetchStyle.checkmark != null
                ? fetchStyle.checkmark.toTheme() : new CheckMarkTheme());
        largeFont.applyNewStyle(fetchStyle.largeFont != null
                ? fetchStyle.largeFont.toTheme(new FontLargeTheme()) : new FontLargeTheme());
        mediumFont.applyNewStyle(fetchStyle.mediumFont != null
                ? fetchStyle.mediumFont.toTheme(new FontMediumTheme()) : new FontMediumTheme());
        smallFont.applyNewStyle(fetchStyle.smallFont != null
                ? fetchStyle.smallFont.toTheme(new FontSmallTheme()) : new FontSmallTheme());
        freeText.applyNewStyle(
                fetchStyle.freeText != null ? fetchStyle.freeText.toTheme() : new FreeTextTheme());
        brand.applyNewStyle(
                fetchStyle.branding != null ? fetchStyle.branding.toTheme() : new BrandTheme());
        invite.applyNewStyle(
                fetchStyle.invite != null ? fetchStyle.invite.toTheme() : new InviteTextTheme());
        question.applyNewStyle(fetchStyle.question != null
                ? fetchStyle.question.toTheme() : new QuestionTextTheme());
        questionError.applyNewStyle(fetchStyle.questionError != null
                ? fetchStyle.questionError.toTheme() : new QuestionErrorTextTheme());
        surveyImg.applyNewStyle(fetchStyle.surveyImg != null
                ? fetchStyle.surveyImg.toTheme(new SurveyImageTheme()) : new SurveyImageTheme());
        ansImg.applyNewStyle(fetchStyle.ansImg != null
                ? fetchStyle.ansImg.toTheme(new ResponseImageTheme()) : new ResponseImageTheme());
        pollBar.applyNewStyle(
                fetchStyle.pollBar != null ? fetchStyle.pollBar.toTheme() : new PollBarTheme());
        helper.applyNewStyle(
                fetchStyle.helper != null ? fetchStyle.helper.toTheme() : new HelperTheme());
    }

    public boolean hideLogo = false;
    public String backgroundColor = "#FFFFFF";
    public String textColor = "#000000";
    public String tableBorderColor = "#858585";
    public String buttonColor = "#1274B8";
    public String buttonTextColor = "#FFFFFF";
    public String radioButtonColor = "#000000";
    public String widgetButtonTextColor = "#079DDC";
    public String pollBarColor = "#676767";

    public void applyNewStyle(ThemeStyles newStyle) {
        this.hideLogo = newStyle.hideLogo;
        this.backgroundColor = newStyle.backgroundColor;
        this.textColor = newStyle.textColor;
        this.tableBorderColor = newStyle.tableBorderColor;
        this.buttonColor = newStyle.buttonColor;
        this.buttonTextColor = newStyle.buttonTextColor;
        this.radioButtonColor = newStyle.radioButtonColor;
        this.widgetButtonTextColor = newStyle.widgetButtonTextColor;
        this.pollBarColor = newStyle.pollBarColor;
    }

    public void resetStyle() {
        this.applyNewStyle(new ThemeStyles());
    }

}
