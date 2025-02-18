package com.pulseinsights.surveysdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.pulseinsights.surveysdk.data.model.SurveyCover;
import com.pulseinsights.surveysdk.motion.CusShakeDetector;
import com.pulseinsights.surveysdk.util.DebugTool;
import com.pulseinsights.surveysdk.util.FormatSetTool;
import com.pulseinsights.surveysdk.util.PreferencesManager;
import com.pulseinsights.surveysdk.util.SurveyAnsweredListener;
import com.pulseinsights.surveysdk.util.SurveyFlowResult;
import com.pulseinsights.surveysdk.util.SurveyInlineResult;
import com.pulseinsights.surveysdk.util.SurveyMainView;
import com.pulseinsights.surveysdk.util.SurveyViewResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LeoChao on 2016/10/6.
 */

public class PulseInsights {

    Context context;
    PulseInsightsApi pulseInsightsApi = null;
    private static PulseInsights instant;
    private SurveyAnsweredListener surveyAnsweredListener = null;

    public PulseInsights(Context context, String accountId, ExtraConfig config) {
        initPulseInsights(context, accountId, config);
    }

    public PulseInsights(Context context, String accountId) {
        initPulseInsights(context, accountId, new ExtraConfig());
    }

    private void initPulseInsights(Context context, String accountId, ExtraConfig config) {
        LocalData.instant.surveyWatcherEnable = config.automaticStart;
        LocalData.instant.previewMode = config.previewMode;
        LocalData.instant.customData =
                config.customData != null ? config.customData : new HashMap<String, String>();
        initialItem(context);
        configAccountId(accountId);
        instant = this;
    }

    public void configAccountId(String accountId) {
        LocalData.instant.isSurveyApiRunning = true;
        LocalData.instant.strAccountId = accountId;
        if (!accountId.isEmpty()) {
            getUdid();
            LocalData.instant.surveyEventCode = Define.PULSE_INSIGHTS_EVENT_CODE_ACCOUNT_RESETED;
        }
        LocalData.instant.isSurveyApiRunning = false;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        initialItem(context);
    }

    public void logAnswered(String surveyId) {
        PreferencesManager.getInstance(context).logAnsweredSurvey(surveyId);
        if (surveyAnsweredListener != null) {
            surveyAnsweredListener.onAnswered(surveyId);
        }
    }

    public void setAnswerListener(SurveyAnsweredListener listener) {
        surveyAnsweredListener = listener;
    }


    public void finishInlineMode() {
        surveyInlineResult = null;
        setScanFrequency(LocalData.instant.timerDurationInSecond);
        LocalData.instant.surveyEventCode = Define.PULSE_INSIGHTS_EVENT_CODE_SURVEY_JUST_CLOSED;
        LocalData.instant.mirrorTimer.start();
    }

    public void setPreviewMode(boolean enable) {
        if (enable) {
            Toast.makeText(context,
                    context.getString(R.string.msgPreviewOn), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,
                    context.getString(R.string.msgPreviewOff), Toast.LENGTH_SHORT).show();
        }
        LocalData.instant.previewMode = enable;
    }

    public boolean isPreviewModeOn() {
        return LocalData.instant.previewMode;
    }

    public void setClientKey(String clientKey) {
        PreferencesManager.getInstance(context).setClientKey(clientKey);
    }

    public String getClientKey() {
        return PreferencesManager.getInstance(context).getClientKey();
    }

    public static PulseInsights getInstant() {
        return instant;
    }

    private void initialItem(Context context) {
        this.context = context;
        pulseInsightsApi = new PulseInsightsApi(this.context, surveyFlowResult);
        setupMotionSensor();
        if (!LocalData.instant.isAppStarted) {
            PreferencesManager.getInstance(this.context).addLaunchCount();
            LocalData.instant.isAppStarted = true;
            DebugTool.debugPrintln(this.context, "Add launch count");
        }
        DebugTool.debugPrintln(this.context, "Launch count :" + String.valueOf(
                PreferencesManager.getInstance(this.context).getLaunchCount()));
        getAppInstallDays();
        if (LocalData.instant.mirrorTimer == null) {
            setScanFrequency(LocalData.instant.timerDurationInSecond);
            LocalData.instant.mirrorTimer.start();
        }
    }

    private CusShakeDetector cusShakeDetector = null;

    public void setupMotionSensor() {
        cusShakeDetector = new CusShakeDetector();
        cusShakeDetector.setTriggerThreshold(10);
        cusShakeDetector.setOnShakeListener(new CusShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                setPreviewMode(!isPreviewModeOn());
            }
        });
        SensorManager sensorManager =
                (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        cusShakeDetector.start(sensorManager);
    }

    public boolean checkSurveyAnswered(String surveyId) {
        return PreferencesManager.getInstance(context).isSurveyAnswered(surveyId);
    }

    public void setScanFrequency(int frequencyInSecond) {
        if (LocalData.instant.mirrorTimer != null) {
            LocalData.instant.mirrorTimer.cancel();
            LocalData.instant.mirrorTimer = null;
        }
        if (frequencyInSecond > 0) {
            LocalData.instant.timerDurationInSecond = frequencyInSecond;
            LocalData.instant.mirrorTimer = new CountDownTimer(frequencyInSecond * 1000, 10) {
                @Override
                public void onTick(long val) {
                    //TODO:onTick event
                }

                @Override
                public void onFinish() {
                    if (LocalData.instant.surveyWatcherEnable) {
                        boolean needServe = false;
                        if (LocalData.instant.surveyEventCode
                                != Define.PULSE_INSIGHTS_EVENT_CODE_NORMAL) {
                            if (LocalData.instant.surveyEventCode
                                    == Define.PULSE_INSIGHTS_EVENT_CODE_SURVEY_JUST_CLOSED) {
                                closeSurvey();
                            } else if (LocalData.instant.surveyEventCode
                                    == Define.PULSE_INSIGHTS_EVENT_CODE_ACCOUNT_RESETED) {
                                needServe = true;
                            }
                            LocalData.instant.surveyEventCode =
                                    Define.PULSE_INSIGHTS_EVENT_CODE_NORMAL;
                        }

                        if (!LocalData.instant.isSurveyApiRunning) {
                            if (checkConditionRunServe()) {
                                needServe = true;
                            }
                        }

                        if (needServe) {
                            serve();
                        }
                    }
                    LocalData.instant.mirrorTimer.start();
                }
            };
        }

    }

    public boolean isSurveyRenderingActive() {
        return LocalData.instant.surveyWatcherEnable;
    }

    public void switchSurveyScan(boolean enable) {
        LocalData.instant.surveyWatcherEnable = enable;
    }

    public void setHost(String hostName) {
        if (!hostName.startsWith("http")) {
            hostName = "https://" + hostName;
        }
        PreferencesManager.getInstance(context).changeHostUrl(hostName);
    }

    private SurveyInlineResult surveyInlineResult;

    public void serve() {
        LocalData.instant.isSurveyApiRunning = true;
        LocalData.instant.installDays = getAppInstallDays();
        pulseInsightsApi.serve();
    }


    public void setDeviceData(Map<String, String> map) {
        pulseInsightsApi.setDeviceData(map);
    }

    public void setContextData(Map<String, String> data, boolean merge) {
        if (merge) {
            Map<String, String> currentData = LocalData.instant.customData;
            currentData.putAll(data);
            LocalData.instant.customData = currentData;
        } else {
            LocalData.instant.customData = data;
        }
    }

    private void closeSurvey() {
        LocalData.instant.isSurveyApiRunning = true;
        pulseInsightsApi.postClose();
    }

    public void present(String surveyId) {
        LocalData.instant.strCheckingSurveyId = surveyId;
        LocalData.instant.isSurveyApiRunning = true;
        pulseInsightsApi.getSurveyInformation();
    }

    public void setDebugMode(boolean enable) {
        PreferencesManager.getInstance(context).changeDebugModeSetting(enable);
    }

    public void resetUdid() {
//        if (!LocalData.instant.isSurveyApiRunning) {
        LocalData.instant.isSurveyApiRunning = true;
        PreferencesManager.getInstance(context).resetDeviceUdid();
        getUdid();
        LocalData.instant.surveyEventCode = Define.PULSE_INSIGHTS_EVENT_CODE_ACCOUNT_RESETED;
        LocalData.instant.isSurveyApiRunning = false;
//        } else {
//            DebugTool.debugPrintln(context, "resetUdid",
//                    "Can not reset UDID because the active survey not end yet");
//        }
    }

    public void setViewName(String viewName) {
        LocalData.instant.runningViewName = viewName;
        LocalData.instant.strViewName = viewName;
    }

    private boolean checkConditionRunServe() {
        boolean rtVal = false;

        int tmpInstallDays = getAppInstallDays();
        if (LocalData.instant.installDays != tmpInstallDays) {
            rtVal = true;
        }
        LocalData.instant.installDays = tmpInstallDays;
        String strTmpViewName = LocalData.instant.runningViewName;
        if ((!strTmpViewName.equalsIgnoreCase(LocalData.instant.strViewName))
                && !strTmpViewName.isEmpty()) {
            rtVal = true;
        }
        LocalData.instant.strViewName = strTmpViewName;
        if (LocalData.instant.strAccountId.isEmpty()) {
            rtVal = false;
        }

        return rtVal;
    }

    private void getUdid() {
        LocalData.instant.strUdid = PreferencesManager.getInstance(context).getDeviceUdid();
        if (LocalData.instant.strUdid.isEmpty()) {
            LocalData.instant.strUdid = Udid.getUdid();
            PreferencesManager.getInstance(context).saveDeviceUdid(LocalData.instant.strUdid);
        }
    }

    private int getAppInstallDays() {
        int rtVal = 0;
        try {
            long installed =
                    context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
                            .firstInstallTime;
            long nowtime = System.currentTimeMillis();
            long difftime = nowtime - installed;
            int installDays = (int) (difftime / (1000 * 60 * 60 * 24));
            rtVal = installDays;
            DebugTool.debugPrintln(context,
                    String.format("Get the install days of current app => %d days", installDays));
        } catch (PackageManager.NameNotFoundException exception) {
            exception.printStackTrace();
        }

        return rtVal;
    }

    private void delayDisplaySurvey() {
        if (LocalData.instant.delayTriggerTimer != null) {
            LocalData.instant.delayTriggerTimer.cancel();
            LocalData.instant.delayTriggerTimer = null;
        }
        boolean enableDelayTrigger = LocalData.instant.surveyPack.survey.enablePendingStart;
        int assignDelayTime = (enableDelayTrigger)
                ? 1000 * LocalData.instant.surveyPack.survey.pendingStartTime : 0;
        DebugTool.debugPrintln(context, "Timer test",
                String.format("delay timer setup, assign times => %d ms", assignDelayTime));
        LocalData.instant.delayTriggerTimer = new CountDownTimer(assignDelayTime, 10) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                DebugTool.debugPrintln(context, "Timer test", "delay timer triggered");
                displaySurvey();
            }
        };
        LocalData.instant.delayTriggerTimer.start();
    }

    private boolean isClearDisplay() {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            return !(activity.isFinishing() || activity.isDestroyed());

        } else {
            return false;
        }
    }

    private void displaySurvey() {
        if (surveyInlineResult != null) {
            surveyInlineResult.onServeResult();
        } else {
            if (context instanceof Activity) {
                SurveyCover surveyObj = LocalData.instant.surveyPack.survey;

                if (isClearDisplay()) {
                    if (surveyObj.invitation.isEmpty()) {
                        if (surveyObj.surveyType == 0) {
                            startSurveyActivity();
                        } else {
                            showPopupTypeWidget();
                        }
                    } else {
                        showInviteWidget(surveyObj);
                    }
                    pulseInsightsApi.viewedAt();
                }
            } else {
                DebugTool.debugPrintln(context, "PI/displaySurvey",
                        "We need the context from Activity object for display "
                                + "the survey view and the invitation widget, please make "
                                + "sure you provide one via pi.setContext(Context context).");
            }
        }
    }

    ConstraintLayout widgetBody;
    TextView widgetMsgTxt;
    RelativeLayout widgetMsgContainer;
    TextView widgetBtnTxt;
    ImageView surveyIcon;
    RelativeLayout inviteGoButton;
    RelativeLayout surveyIconContainer;
    SurveyMainView widgetSurvey;

    private void showPopupTypeWidget() {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pulse_insight_widget_survey, null);

        // Get the screen height for define the container size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widgetHeight = LocalData.instant.surveyPack.survey.widgetHeight;
        int popupViewHeight = widgetHeight <= 0
                ? displayMetrics.heightPixels / 2
                : displayMetrics.heightPixels * Math.min(widgetHeight, 100) / 100;
        final PopupWindow tmpWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT, popupViewHeight);
        final View rootView = ((Activity) context).getWindow()
                .getDecorView().findViewById(android.R.id.content);
        final int[] location = new int[2];
        rootView.getLocationOnScreen(location);
        tmpWindow.setTouchable(true);
        tmpWindow.setFocusable(true);
        tmpWindow.setOutsideTouchable(true);
        tmpWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    tmpWindow.dismiss();
                    LocalData.instant.surveyEventCode =
                            Define.PULSE_INSIGHTS_EVENT_CODE_SURVEY_JUST_CLOSED;
                    return true;
                }
                return false;
            }
        });
        widgetSurvey = (SurveyMainView) view.findViewById(R.id.widget_survey);
        widgetSurvey.setCallback(new SurveyViewResult() {
            @Override
            public void onFinish() {
                tmpWindow.dismiss();
                LocalData.instant.surveyEventCode =
                        Define.PULSE_INSIGHTS_EVENT_CODE_SURVEY_JUST_CLOSED;
            }
        });
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (isClearDisplay()) {
                    widgetSurvey.setupSurveyContent(LocalData.instant.surveyTickets);
                    tmpWindow.showAtLocation(rootView,
                            Gravity.START | Gravity.BOTTOM, 0, -location[1]);
                } else {
                    tmpWindow.dismiss();
                    LocalData.instant.surveyEventCode =
                            Define.PULSE_INSIGHTS_EVENT_CODE_SURVEY_JUST_CLOSED;
                }
            }

        }, 100L);
    }


    private void showInviteWidget(SurveyCover surveyObj) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pulse_insight_widget_invitation, null);
        final PopupWindow tmpWindow = new PopupWindow(
                view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                false);
        final View rootView = ((Activity) context).getWindow()
                .getDecorView().findViewById(android.R.id.content);
        final int[] location = new int[2];
        rootView.getLocationOnScreen(location);
        tmpWindow.setTouchable(true);
        tmpWindow.setOutsideTouchable(true);
        tmpWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    tmpWindow.dismiss();
                    LocalData.instant.surveyEventCode =
                            Define.PULSE_INSIGHTS_EVENT_CODE_SURVEY_JUST_CLOSED;
                    return true;
                }
                return false;
            }
        });
        inviteGoButton = (RelativeLayout) view.findViewById(R.id.btn_submit);
        LocalData.instant.themeStyles.submitBtn.setupLayout(context, inviteGoButton);
        widgetBtnTxt = (TextView) view.findViewById(R.id.widget_go_btn);
        LocalData.instant.themeStyles.submitBtn.configButtonText(widgetBtnTxt);
        if (surveyObj.invitationButtonDisable) {
            widgetBtnTxt.setVisibility(View.GONE);
        } else {
            widgetBtnTxt.setVisibility(View.VISIBLE);
        }
        if (surveyObj.invitationButton.isEmpty()) {
            widgetBtnTxt.setText("Start");
        } else {
            widgetBtnTxt.setText(surveyObj.invitationButton);
        }
        widgetMsgTxt = (TextView) view.findViewById(R.id.widget_msg_txt);
        widgetMsgContainer = (RelativeLayout) view.findViewById(R.id.widget_msg_container);
        LocalData.instant.themeStyles.invite.applyMargin(context, widgetMsgContainer);
        surveyIcon = (ImageView) view.findViewById(R.id.survey_icon);
        surveyIconContainer = (RelativeLayout) view.findViewById(R.id.survey_icon_container);
        String imageUrl = LocalData.instant.surveyPack.survey.background;
        if (imageUrl == null || imageUrl.isEmpty()) {
            surveyIconContainer.setVisibility(View.GONE);
        } else {
            LocalData.instant.themeStyles.surveyImg.configImageView(context, surveyIcon, imageUrl);
            LocalData.instant.themeStyles.surveyImg.configImageContainer(surveyIconContainer);
        }
        LocalData.instant.themeStyles.largeFont.configText(widgetMsgTxt);
        FormatSetTool.setTextByHtml(widgetMsgTxt, surveyObj.invitation);
        widgetMsgTxt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        widgetBody = view.findViewById(R.id.widget_body);
        LocalData.instant.themeStyles.widget.configLayout(context, widgetBody);
        final int surveyType = surveyObj.surveyType;
        widgetBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tmpWindow.dismiss();
                if (surveyType == 0) {
                    startSurveyActivity();
                } else {
                    showPopupTypeWidget();
                }
            }
        });
        TextView closeSymbol = (TextView) view.findViewById(R.id.widget_close_symbol);
        closeSymbol.setTextColor(
                Color.parseColor(LocalData.instant.themeStyles.closeBtn.fontColor));
        RelativeLayout widgetCloseArea = (RelativeLayout) view.findViewById(R.id.widget_close_area);
        LocalData.instant.themeStyles.closeBtn.setBtnContainer(context, widgetCloseArea);
        widgetCloseArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tmpWindow.dismiss();
                LocalData.instant.surveyEventCode =
                        Define.PULSE_INSIGHTS_EVENT_CODE_SURVEY_JUST_CLOSED;
            }
        });

        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (isClearDisplay()) {
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    ((Activity) getContext()).getWindowManager()
                            .getDefaultDisplay().getMetrics(displayMetrics);
                    view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    int popupHeight = view.getMeasuredHeight();
                    int yOffset = displayMetrics.heightPixels - popupHeight;
                    tmpWindow.showAtLocation(rootView,
                            Gravity.START | Gravity.BOTTOM, 0, -location[1]);
                } else {
                    tmpWindow.dismiss();
                    LocalData.instant.surveyEventCode =
                            Define.PULSE_INSIGHTS_EVENT_CODE_SURVEY_JUST_CLOSED;
                }
            }

        }, 100L);
    }

    private void startSurveyActivity() {
        Intent intent = new Intent(context, SurveyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    SurveyFlowResult surveyFlowResult = new SurveyFlowResult() {

        @Override
        public void result(String strAnswer, String strNextDirection) {
            if (strAnswer.equalsIgnoreCase(Define.SURVEY_REQ_TYPE_PRESENT)
                    || strAnswer.equalsIgnoreCase(Define.SURVEY_REQ_TYPE_SCAN)) {
                if (!LocalData.instant.surveyPack.survey.id.isEmpty()) {
                    pulseInsightsApi.getQuestionDetail();
                } else {
                    LocalData.instant.isSurveyApiRunning = false;
                }
            } else if (strAnswer.equalsIgnoreCase(Define.SURVEY_REQ_TYPE_GETCONTENT)) {
                String trackId = LocalData.instant.surveyPack.survey.inlineTrackId;
                boolean noInlineTrigView = LocalData.instant.surveyPack.survey.surveyType != 1;
                boolean doInline = false;
                if (!noInlineTrigView && LocalData.instant.inlineLink.containsKey(trackId)) {
                    SurveyInlineResult resultCb = LocalData.instant.inlineLink.get(trackId);
                    if (resultCb.onDisplay()) { // Check if the inline view is onDisplay
                        doInline = true;
                        surveyInlineResult = resultCb;
                    }
                }
                if (noInlineTrigView || doInline) {
                    delayDisplaySurvey();
                } else {
                    LocalData.instant.isSurveyApiRunning = false;
                }
            } else if (strAnswer.equalsIgnoreCase(Define.SURVEY_REQ_TYPE_SENDANSWER)) {
                //TODO: SURVEY_REQ_TYPE_SENDANSWER
            } else if (strAnswer.equalsIgnoreCase(Define.SURVEY_REQ_TYPE_CLOSE)) {
                if (surveyInlineResult != null) {
                    surveyInlineResult.onFinish();
                }
                LocalData.instant.isSurveyApiRunning = false;
            } else if (strAnswer.equalsIgnoreCase("error")) {
                if (surveyInlineResult != null) {
                    surveyInlineResult.onFinish();
                }
                LocalData.instant.isSurveyApiRunning = false;
            }
        }
    };
}
