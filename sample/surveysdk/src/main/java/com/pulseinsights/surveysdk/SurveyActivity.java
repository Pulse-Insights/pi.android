package com.pulseinsights.surveysdk;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.pulseinsights.surveysdk.util.SurveyMainView;
import com.pulseinsights.surveysdk.util.SurveyViewResult;


public class SurveyActivity extends Activity {
    View layout;
    RelativeLayout piBaseArea;
    SurveyMainView surveyMainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater layoutInflater = getLayoutInflater();
        layout = layoutInflater.inflate(R.layout.pulse_insight_survey_activity, null);
        init();

        setContentView(layout);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        startSurvey();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        super.onTouchEvent(event);

        return false;

    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalData.instant.surveyEventCode = Define.PULSE_INSIGHTS_EVENT_CODE_SURVEY_JUST_CLOSED;
        //TODO:Maybe need a flow to shutdown the http core inside mPulseInsightMain...?
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, 0);
    }



    void init() {
        piBaseArea = (RelativeLayout) layout.findViewById(R.id.pi_base_area);
        surveyMainView = (SurveyMainView) layout.findViewById(R.id.pi_survey_area);
        surveyMainView.setCallback(new SurveyViewResult() {
            @Override
            public void onFinish() {
                finish();
            }
        });
    }

    private void startSurvey() {
        surveyMainView.setupSurveyContent(LocalData.instant.surveyTickets);
    }

}
