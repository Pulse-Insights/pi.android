package com.pulseinsights.surveysdk;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.pulseinsights.surveysdk.data.model.SurveyTicket;
import com.pulseinsights.surveysdk.util.EventListener;
import com.pulseinsights.surveysdk.util.OnAnswerSelectedListener;
import com.pulseinsights.surveysdk.util.SurveyAnswers;
import com.pulseinsights.surveysdk.util.SurveyItemView;
import com.pulseinsights.surveysdk.util.SurveyMainView;
import com.pulseinsights.surveysdk.util.SurveyViewResult;


public class SurveyActivity extends Activity {
    View layout;
    RelativeLayout piBaseArea;
    SurveyMainView surveyMainView;

    ScrollView allAtOnceLayout;
    Button submitAllButton;
    private SurveyAnswers surveyAnswers;
    PulseInsightsApi pulseInsightsApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater layoutInflater = getLayoutInflater();
        layout = layoutInflater.inflate(R.layout.pulse_insight_survey_activity, null);
        init();
        this.surveyAnswers = new SurveyAnswers();
        pulseInsightsApi = new PulseInsightsApi(this);

        setContentView(layout);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

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
        allAtOnceLayout = (ScrollView) layout.findViewById(R.id.allAtOnceLayout);
        submitAllButton = (Button) layout.findViewById(R.id.submitAllButton);
        LocalData.instant.themeStyles.submitBtn.configButtonText(submitAllButton);
        LocalData.instant.themeStyles.submitBtn.setupLayout(this, submitAllButton);
        submitAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("SurveyActivity all answers", surveyAnswers.toString());
                // need to check if any question is not answered
                LinearLayout questionList = (LinearLayout)allAtOnceLayout.getChildAt(0);
                if(LocalData.instant.surveyPack.survey.allAtOnceEmptyErrorEnabled) {
                    // check each question is optional or not, and if it's not and not answered, show error
                    String error = "";
                    for (int i = 0; i < LocalData.instant.surveyTickets.size(); i++) {
                        SurveyTicket surveyTicket = LocalData.instant.surveyTickets.get(i);
                        SurveyItemView item = (SurveyItemView)questionList.getChildAt(i);
                        if (!surveyTicket.optional && !surveyAnswers.isAnswered(surveyTicket.id)) {
                            // show error
                            item.showError(surveyTicket.emptyError);
                            error = surveyTicket.emptyError;
                        } else {
                            item.showError("");
                        }
                    }
                    if (error.isEmpty()) {
                        postAllAtOnce();
                    }
                } else if(surveyAnswers.isEmpty()) {
                    Toast.makeText(SurveyActivity.this, "Please answer at least one question", Toast.LENGTH_SHORT).show();
                }else {
                    postAllAtOnce();
                }
            }
        });
    }

    private void postAllAtOnce() {
        pulseInsightsApi.postAllAtOnce(surveyAnswers, new EventListener() {
            @Override
            public void onEvent(Object result) {
                Log.e("SurveyActivity", "onEvent: " + result);
                finish();
            }
        });
    }

    private void startSurvey() {
        if (LocalData.instant.surveyPack.survey.displayAllQuestions) {
            allAtOnceLayout.setVisibility(View.VISIBLE);
            surveyMainView.setVisibility(View.GONE);
            piBaseArea.setVisibility(View.GONE);
            submitAllButton.setVisibility(View.VISIBLE);
            if (LocalData.instant.surveyPack.survey.allAtOnceSubmitLabel != null) {
                submitAllButton.setText(LocalData.instant.surveyPack.survey.allAtOnceSubmitLabel);
            }
            allAtOnceLayout.removeAllViews();
            // prepare a linear layout to hold all the questions
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            // Set the layout parameters
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  // Width
                    LinearLayout.LayoutParams.WRAP_CONTENT   // Height
            );
            linearLayout.setLayoutParams(layoutParams);
            allAtOnceLayout.addView(linearLayout);
            for (int i = 0; i < LocalData.instant.surveyTickets.size(); i++) {
                SurveyItemView questionView = getQuestionView(i);
                if (i == 0) {
                    questionView.setCallback(new SurveyViewResult() {
                        @Override
                        public void onFinish() {
                            finish();
                        }
                    });
                }
                linearLayout.addView(questionView);
            }
        } else {
            allAtOnceLayout.setVisibility(View.GONE);
            surveyMainView.setupSurveyContent(LocalData.instant.surveyTickets);
        }
    }

    @NonNull
    private SurveyItemView getQuestionView(int i) {
        SurveyTicket surveyTicket = LocalData.instant.surveyTickets.get(i);
        SurveyItemView questionView = new SurveyItemView(this, i == 0);
        questionView.setOnAnswerSelectedListener(new OnAnswerSelectedListener() {
            @Override
            public void onAnswerSelected(String answer) {
                // need to save question_id, question_type and answer
                surveyAnswers.setAnswer(surveyTicket.id, surveyTicket.questionType, answer);
            }
        });
        questionView.setSurveyType(surveyTicket);
        return questionView;
    }

}
