package com.pulseinsights.surveysdk.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.pulseinsights.surveysdk.LocalData;
import com.pulseinsights.surveysdk.R;

/**
 * Created by LeoChao on 2016/10/13.
 */

public class PartCustomContentType extends RelativeLayout {
    Context context;
    View layout;
    WebView freeSection;
    SurveyFlowResult surveyFlowResult;
    private boolean isItemAlive = false;
    String htmlString = "<html><head></head><style type='text/css'>@font-face { \n"
            + "    font-family: \"HelveticaNeue\"; \n"
            + "    src: url('fonts/HelveticaNeue-Light.otf');} \n"
            + "p { font-family: 'HelveticaNeue', Helvetica, Arial, sans-serif; "
            + "text-align: justify; font-size: 16px; color:%s; }"
            + "span {color:%s;} " + "body { margin: 25px 25px 25px 25px;} "
            + "a { color:%s; text-decoration:none;} </style>"
            + "<body bgcolor=\"transparent\">%s</body></html>";

    public PartCustomContentType(Context context) {
        super(context);
        init(context);
    }

    public PartCustomContentType(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    void init(Context context) {
        this.context = context;
        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = layoutInflater.inflate(R.layout.pulse_insight_survey_customcontenttype, this);
        freeSection = (WebView) layout.findViewById(R.id.free_section);
        freeSection.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        freeSection.getSettings().setJavaScriptEnabled(true);
        freeSection.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        freeSection.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        freeSection.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        freeSection.setBackgroundColor(0);
        freeSection.getBackground().setAlpha(0);
    }

    private void returnByCallBack(String strResult) {
        if (surveyFlowResult != null) {
            surveyFlowResult.result("custom_content_question", strResult);
        }
    }

    CountDownTimer timerClose;
    CountDownTimer timerRedirect;

    public void setItemContent(String strHtmlContent, final String strUrl,
                               int autoCloseTime, int autoDirectTime,
                               SurveyFlowResult surveyFlowResult) {
        isItemAlive = true;
        this.surveyFlowResult = surveyFlowResult;
        timerClose = null;
        timerRedirect = null;
        setFreeContent(strHtmlContent);
        if (autoCloseTime > 0) {
            timerClose =
                    new CountDownTimer(autoCloseTime * 1000, 100) {
                        @Override
                        public void onTick(long val) {

                        }

                        @Override
                        public void onFinish() {
                            isItemAlive = false;
                            if (timerRedirect != null) {
                                timerRedirect.cancel();
                            }
                            returnByCallBack("close");
                        }
                    };
            timerClose.start();
        }
        if (autoDirectTime > 0) {
            timerRedirect =
                    new CountDownTimer(autoDirectTime * 1000, 100) {
                        @Override
                        public void onTick(long val) {

                        }

                        @Override
                        public void onFinish() {
                            if (isItemAlive) {
                                setLoadedUrl(strUrl);
                            }
                        }
                    };
            timerRedirect.start();
        }

    }

    private void setFreeContent(String strHtmlContent) {
        String strTextCode = LocalData.instant.themeStyles.textColor;

        freeSection.loadDataWithBaseURL("file:///android_asset/",
                String.format(htmlString, strTextCode, strTextCode, strTextCode, strHtmlContent),
                "text/html",
                "utf-8", null);
    }

    private void setLoadedUrl(String strUrl) {
        freeSection.loadUrl(strUrl);
    }
}
