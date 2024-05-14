package com.pulseinsights.surveysdk.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pulseinsights.surveysdk.LocalData;
import com.pulseinsights.surveysdk.PulseInsights;
import com.pulseinsights.surveysdk.PulseInsightsApi;
import com.pulseinsights.surveysdk.R;
import com.pulseinsights.surveysdk.data.model.PollResult;
import com.pulseinsights.surveysdk.data.model.SurveyCover;
import com.pulseinsights.surveysdk.data.model.SurveyTicket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leochao on 2018/2/22.
 */

public class SurveyMainView extends RelativeLayout {
    Context context;
    View layout;
    TextView piPageTitle;
    TextView closeSymbol;
    RelativeLayout titleContainer;
    TextView beforeHelperText;
    RelativeLayout beforeHelperContainer;
    LinearLayout beforeAnswerContainer;
    TextView afterHelperText;
    RelativeLayout afterHelperContainer;
    LinearLayout afterAnswerContainer;
    PartChoiceType piContentChoiceSingle;
    PartChoiceType piContentChoiceMultiple;
    PartFreeTextType piContentText;
    PartCustomContentType piContentCustom;
    SurveyPollResult piPollResult;
    RelativeLayout piAreaSubmit;
    RelativeLayout piThanksArea;
    RelativeLayout btnSubmit;
    RelativeLayout btnClose;
    RelativeLayout logoView;
    RelativeLayout surveyIconContainer;
    RelativeLayout innerInviteView;
    RelativeLayout inviteTextContainer;
    RelativeLayout inviteSubmitContainer;
    TextView inviteMsgText;
    TextView inviteSubmitLabel;

    LinearLayout piSurveyContent;
    TextView btnSubmitTxt;
    TextView txtThanksMsg;
    RelativeLayout piSurveyRoot;
    PulseInsightsApi pulseInsightsApi;
    PulseInsights pulseInsights;
    SurveyViewResult surveyViewResult;
    ImageView surveyIcon;

    boolean inlineEnable = false;
    boolean inlineType = false;
    String trackIdentify = "";

    public void switchInlineType(boolean enable) {
        inlineType = enable;
    }

    public SurveyMainView(Context context) {
        super(context);
        init(context);
    }

    public SurveyMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    void init(Context context) {
        this.context = context;
        pulseInsights = PulseInsights.getInstant();
        pulseInsightsApi = new PulseInsightsApi(this.context);
        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = layoutInflater.inflate(R.layout.pulse_insight_survey_main, this);

        innerInviteView = layout.findViewById(R.id.inner_invite);
        inviteTextContainer = layout.findViewById(R.id.invite_msg_container);
        inviteSubmitContainer = layout.findViewById(R.id.invite_submit_container);
        inviteMsgText = layout.findViewById(R.id.invite_msg_txt);
        inviteSubmitLabel = layout.findViewById(R.id.invite_submit_label);

        piSurveyRoot = layout.findViewById(R.id.pi_survey_root);
        LocalData.instant.themeStyles.widget.configLayout(context, piSurveyRoot);
        piPageTitle = layout.findViewById(R.id.pi_page_title);
        LocalData.instant.themeStyles.largeFont.configText(piPageTitle);
        titleContainer = layout.findViewById(R.id.page_title_container);
        LocalData.instant.themeStyles.question.applyMargin(context, titleContainer);

        beforeHelperText = layout.findViewById(R.id.before_helper);
        LocalData.instant.themeStyles.mediumFont.configText(beforeHelperText);
        beforeHelperContainer = layout.findViewById(R.id.before_helper_container);
        LocalData.instant.themeStyles.helper.applyMargin(context, beforeHelperContainer);

        afterHelperText = layout.findViewById(R.id.after_helper);
        LocalData.instant.themeStyles.mediumFont.configText(afterHelperText);
        afterHelperContainer = layout.findViewById(R.id.after_helper_container);

        beforeAnswerContainer = layout.findViewById(R.id.beforeAnswerContainer);
        afterAnswerContainer = layout.findViewById(R.id.afterAnswerContainer);
        LocalData.instant.themeStyles.helper.applyMargin(context, afterHelperContainer);
        titleContainer.setVisibility(GONE);
        beforeHelperContainer.setVisibility(GONE);
        beforeAnswerContainer.setVisibility(GONE);
        afterHelperContainer.setVisibility(GONE);
        afterAnswerContainer.setVisibility(GONE);

        surveyIcon = layout.findViewById(R.id.survey_icon);
        piContentChoiceSingle = layout.findViewById(R.id.pi_content_choice_single);
        piContentChoiceMultiple =
                layout.findViewById(R.id.pi_content_choice_multiple);
        piContentText = layout.findViewById(R.id.pi_content_text);
        piPollResult = layout.findViewById(R.id.pi_poll_result);
        piContentCustom = layout.findViewById(R.id.pi_content_custom);
        piAreaSubmit = layout.findViewById(R.id.pi_area_submit);
        piThanksArea = layout.findViewById(R.id.pi_thanks_area);
        surveyIconContainer = layout.findViewById(R.id.survey_icon_container);
        txtThanksMsg = layout.findViewById(R.id.txt_thanks_msg);
        LocalData.instant.themeStyles.largeFont.configText(txtThanksMsg);
        btnSubmitTxt = layout.findViewById(R.id.btn_submit_txt);
        btnSubmit = layout.findViewById(R.id.btn_submit);
        configSubmitButton(true);
        piSurveyContent = layout.findViewById(R.id.pi_survey_content);
        logoView = layout.findViewById(R.id.logo_view);
        logoView.setVisibility(
                LocalData.instant.themeStyles.brand.display ? VISIBLE : GONE);
        closeSymbol = layout.findViewById(R.id.close_symbol);
        closeSymbol.setTextColor(
                Color.parseColor(LocalData.instant.themeStyles.closeBtn.fontColor));
        btnClose = layout.findViewById(R.id.btn_close);
        LocalData.instant.themeStyles.closeBtn.setBtnContainer(context, btnClose);
        btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        hideAllArea();
    }

    private void displayInvitation(boolean display) {
        innerInviteView.setVisibility(display ? VISIBLE : GONE);
        if (display) {
            SurveyCover surveyObj = LocalData.instant.surveyPack.survey;
            LocalData.instant.themeStyles.submitBtn.setupLayout(context, inviteSubmitContainer);
            LocalData.instant.themeStyles.submitBtn.configButtonText(inviteSubmitLabel);
            inviteSubmitContainer.setVisibility(
                    surveyObj.invitationButtonDisable ? GONE : VISIBLE);
            inviteSubmitLabel.setText(
                    surveyObj.invitationButton.isEmpty() ? "Start" : surveyObj.invitationButton);
            LocalData.instant.themeStyles.invite.applyMargin(context, inviteTextContainer);
            LocalData.instant.themeStyles.largeFont.configText(inviteMsgText);
            FormatSetTool.setTextByHtml(inviteMsgText, surveyObj.invitation);
            innerInviteView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSurvey(0);
                }
            });
        }
    }

    private void configSubmitButton(boolean enable) {
        LocalData.instant.themeStyles.submitBtn.configButtonText(btnSubmitTxt, enable);
        LocalData.instant.themeStyles.submitBtn.setupLayout(context, btnSubmit, enable);
    }

    public void setCallback(SurveyViewResult callback) {
        surveyViewResult = callback;
    }

    void closeView() {
        if (inlineType) {
            ViewGroup.LayoutParams params = this.getLayoutParams();
            params.height = 0;
            this.setLayoutParams(params);
        }
    }

    private void openView() {
        if (inlineEnable && inlineType) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager()
                    .getDefaultDisplay().getMetrics(displayMetrics);
            int widgetHeight = LocalData.instant.surveyPack.survey.widgetHeight;
            int displayViewHeight = widgetHeight == 0
                    ? displayMetrics.heightPixels / 2
                    : displayMetrics.heightPixels * Math.min(widgetHeight, 100) / 100;
            ViewGroup.LayoutParams params = this.getLayoutParams();
            params.height = displayViewHeight;
            this.setLayoutParams(params);
        }
    }

    public void finish() {
        if (inlineEnable && inlineType) {
            PulseInsights.getInstant().finishInlineMode();
            closeView();
        } else {
            if (surveyViewResult != null) {
                surveyViewResult.onFinish();
            }
        }
    }

    public void openThanksArea(String strThanksText) {
        hideAllArea();
        piThanksArea.setVisibility(VISIBLE);
        FormatSetTool.setTextByHtml(txtThanksMsg, strThanksText);
        final CountDownTimer webViewScanTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long val) {
                //TODO:onTick event
            }

            @Override
            public void onFinish() {
                finish();
            }
        };
        webViewScanTimer.start();

    }

    List<SurveyTicket> surveyItemList = new ArrayList<>();

    public void setupSurveyContent(List<SurveyTicket> inputItem) {
        String imageUrl = LocalData.instant.surveyPack.survey.background;
        LocalData.instant.themeStyles.surveyImg.configImageView(context, surveyIcon, imageUrl);
        LocalData.instant.themeStyles.surveyImg.configImageContainer(surveyIconContainer);

        surveyItemList = new ArrayList<>();
        surveyItemList.addAll(inputItem);
        if (inlineEnable && inlineType && !surveyItemList.isEmpty()) {
            openView();
            if (LocalData.instant.surveyPack.survey.invitation.isEmpty()) {
                showSurvey(0);
            } else {
                displayInvitation(true);
            }
        } else {
            showSurvey(0);
        }
    }

    private void hideAllArea() {
        piSurveyContent.setVisibility(GONE);
        titleContainer.setVisibility(GONE);
        afterHelperContainer.setVisibility(GONE);
        beforeHelperContainer.setVisibility(GONE);
        beforeAnswerContainer.setVisibility(GONE);
        afterAnswerContainer.setVisibility(GONE);
        piThanksArea.setVisibility(GONE);
        displayInvitation(false);
    }

    public void openSurveyArea() {
        hideAllArea();
        piSurveyContent.setVisibility(VISIBLE);
    }

    void showSurvey(int listId) {
        if (listId < surveyItemList.size()) {
            SurveyTicket showItem = surveyItemList.get(listId);
            setSurveyType(showItem);
            if (!showItem.questionType.equalsIgnoreCase("custom_content_question")) {
                titleContainer.setVisibility(showItem.content.isEmpty() ? GONE : VISIBLE);
                FormatSetTool.setTextByHtml(piPageTitle, showItem.content);
            } else {
                titleContainer.setVisibility(GONE);
            }
        }
    }

    void surveyEnd() {
        pulseInsights.logAnswered(LocalData.instant.surveyPack.survey.id);
        String strThanks = LocalData.instant.surveyPack.survey.thankYou;
        if ((strThanks.isEmpty() || strThanks.equalsIgnoreCase("null"))
                && (LocalData.instant.pollResults.isEmpty())) {
            finish();
        } else if (!LocalData.instant.pollResults.isEmpty()) {
            showPollResult();
        } else {
            openThanksArea(strThanks);
        }
    }

    int getListIdByQuestionId(String strQuestionId) {
        int rtVal = -1;

        for (int val = 0; val < surveyItemList.size(); val++) {
            if (surveyItemList.get(val).id.equalsIgnoreCase(strQuestionId)) {
                rtVal = val;
                break;
            }
        }
        return rtVal;
    }

    void goNext(String strNextId) {
        int idInList = getListIdByQuestionId(strNextId);
        if (idInList == -1) {
            surveyEnd();
        } else {
            showSurvey(idInList);
        }
    }

    void showPollResult() {
        openSurveyArea();
        displayInvitation(false);
        piContentChoiceSingle.setVisibility(GONE);
        piContentChoiceMultiple.setVisibility(GONE);
        piContentText.setVisibility(GONE);
        piContentCustom.setVisibility(GONE);
        piAreaSubmit.setVisibility(GONE);
        piPollResult.setVisibility(VISIBLE);
        piPageTitle.setVisibility(VISIBLE);
        beforeHelperContainer.setVisibility(GONE);
        afterHelperContainer.setVisibility(GONE);
        beforeAnswerContainer.setVisibility(GONE);
        afterAnswerContainer.setVisibility(GONE);
        if (!LocalData.instant.pollResults.isEmpty()) {
            PollResult showResult = LocalData.instant.pollResults.get(0);
            piPageTitle.setText(showResult.title);
            piPollResult.setResultItems(showResult);
        }

    }

    public void setSurveyType(SurveyTicket switchItem) {
        openSurveyArea();
        displayInvitation(false);
        piContentChoiceSingle.setVisibility(GONE);
        piContentChoiceMultiple.setVisibility(GONE);
        piContentText.setVisibility(GONE);
        piContentCustom.setVisibility(GONE);
        piPollResult.setVisibility(GONE);
        piAreaSubmit.setVisibility(GONE);
        piPageTitle.setVisibility(VISIBLE);
        piContentText.setOnChangeListener(null);
        piContentChoiceMultiple.setOnChangeListener(null);

        final String strQuizId = switchItem.id;
        final String strQuizType = switchItem.questionType;
        final String strNextId = switchItem.nextQuestionId;

        String beforeHelper = switchItem.beforeHelper;
        String afterHelper = switchItem.afterHelper;

        beforeHelperText.setText(beforeHelper);
        afterHelperText.setText(afterHelper);

        beforeHelperContainer.setVisibility(beforeHelper.isEmpty() ? GONE : VISIBLE);
        afterHelperContainer.setVisibility(afterHelper.isEmpty() ? GONE : VISIBLE);

        if (switchItem.questionType.equalsIgnoreCase("single_choice_question")) {
            if (switchItem.beforeAnswerItems.size() > 0) {
                beforeAnswerContainer.setVisibility(VISIBLE);
                setupAnswerHelper(switchItem.beforeAnswerItems, beforeAnswerContainer);
            } else {
                beforeAnswerContainer.setVisibility(GONE);
            }
            if (switchItem.afterAnswerItems.size() > 0) {
                afterAnswerContainer.setVisibility(VISIBLE);
                setupAnswerHelper(switchItem.afterAnswerItems, afterAnswerContainer);
            } else {
                afterAnswerContainer.setVisibility(GONE);
            }
            piContentChoiceSingle.setVisibility(VISIBLE);
            piContentChoiceSingle.setInitialSet(switchItem, new SurveyFlowResult() {
                @Override
                public void result(String strAnswer, final String strNextDirection) {
                    pulseInsightsApi.postAnswers(
                            strAnswer,
                            strQuizId,
                            strQuizType,
                            new EventListener() {
                                @Override
                                public void onEvent(Object result) {
                                    goNext(strNextDirection);
                                }
                            });
                }
            });
        } else if (switchItem.questionType.equalsIgnoreCase("multiple_choices_question")) {
            piContentChoiceMultiple.setVisibility(VISIBLE);
            piContentChoiceMultiple.setInitialSet(switchItem, null);
            piAreaSubmit.setVisibility(VISIBLE);
            configSubmitButton(piContentChoiceMultiple.clearToSubmit());
            piContentChoiceMultiple.setOnChangeListener(new AnswerOnChange() {
                @Override
                public void onChange() {
                    configSubmitButton(piContentChoiceMultiple.clearToSubmit());
                }
            });
            btnSubmitTxt.setText(switchItem.submitLabel);
            btnSubmit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (piContentChoiceMultiple.clearToSubmit()) {
                        String strGatherAnswers = piContentChoiceMultiple.getMultiAnswer();
                        final String optionNext = piContentChoiceMultiple.multiOptionNext;
                        pulseInsightsApi.postAnswers(strGatherAnswers, strQuizId,
                                strQuizType, new EventListener() {
                                    @Override
                                    public void onEvent(Object result) {
                                        goNext(optionNext.isEmpty() ? strNextId : optionNext);
                                    }
                                });
                    }
                }
            });
        } else if (switchItem.questionType.equalsIgnoreCase("free_text_question")) {
            piContentText.setVisibility(VISIBLE);
            piContentText.setHintText(switchItem.hintText);
            piContentText.setMaxTextLength(switchItem.maxTextLength);
            piAreaSubmit.setVisibility(VISIBLE);
            configSubmitButton(piContentText.clearToSubmit());
            piContentText.setOnChangeListener(new AnswerOnChange() {
                @Override
                public void onChange() {
                    configSubmitButton(piContentText.clearToSubmit());
                }
            });
            //2016-12-07
            //Although no such behavior in javascript version,
            // keep the statement in case we need in the future
            //2016-12-12
            //Confirmed that we should applied Html format in every where of this survey
            FormatSetTool.setTextByHtml(btnSubmitTxt, switchItem.submitLabel);

            btnSubmit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String readyText = piContentText.getInputAnswer();
                    if (piContentText.clearToSubmit()) {
                        InputMethodManager imm = (InputMethodManager) context
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        pulseInsightsApi.postAnswers(readyText, strQuizId, strQuizType,
                                new EventListener() {
                                    @Override
                                    public void onEvent(Object result) {
                                    }
                                });
                        goNext(strNextId);
                    }
                }
            });
        } else if (switchItem.questionType.equalsIgnoreCase("custom_content_question")) {
            piContentCustom.setVisibility(VISIBLE);
            piPageTitle.setVisibility(INVISIBLE);
            beforeHelperContainer.setVisibility(GONE);
            afterHelperContainer.setVisibility(GONE);
            beforeAnswerContainer.setVisibility(GONE);
            afterAnswerContainer.setVisibility(GONE);
            piContentCustom.setItemContent(
                    switchItem.content,
                    switchItem.autoredirectUrl,
                    (switchItem.autocloseEnabled) ? switchItem.autocloseDelay : 0,
                    (switchItem.autoredirectEnabled) ? switchItem.autoredirectDelay : 0,
                    new SurveyFlowResult() {
                        @Override
                        public void result(String strAnswer, String strNextDirection) {
                            goNext("");
                        }
                    });
        }
    }

    void setupAnswerHelper(List<String> helperList, LinearLayout helperContainer) {
        helperContainer.removeAllViews();
        LocalData.instant.themeStyles.helper.applyPadding(context, helperContainer);
        for (int i = 0; i < helperList.size(); i++) {
            String afterAnswer = helperList.get(i);
            TextView tv = new TextView(context);
            LocalData.instant.themeStyles.mediumFont.configText(tv);
            tv.setText(afterAnswer);
            String fontColor = LocalData.instant.themeStyles.helper.fontColor;
            if (!TextUtils.isEmpty(fontColor)) {
                tv.setTextColor(Color.parseColor(fontColor));
            }
            if (helperList.size() == 1) {
                String remoteGravity = LocalData.instant.themeStyles.helper.gravity;
                switch (remoteGravity) {
                    case "left":
                    case "start":
                        tv.setGravity(Gravity.START);
                        tv.setTextAlignment(TEXT_ALIGNMENT_VIEW_START);
                        break;
                    case "right":
                    case "end":
                        tv.setGravity(Gravity.END);
                        tv.setTextAlignment(TEXT_ALIGNMENT_VIEW_END);
                        break;
                    default:
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                        break;
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tv.setLayoutParams(params);
            } else {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.weight = 1;
                if (i == 0) {
                    tv.setGravity(Gravity.START);
                    tv.setTextAlignment(TEXT_ALIGNMENT_VIEW_START);
                } else if (i == helperList.size() - 1) {
                    tv.setGravity(Gravity.END);
                    tv.setTextAlignment(TEXT_ALIGNMENT_VIEW_END);
                } else {
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                }
                tv.setLayoutParams(params);
            }
            helperContainer.addView(tv);
        }
    }
}
