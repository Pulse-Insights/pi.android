package com.pulseinsights.surveysdk.util;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pulseinsights.surveysdk.LocalData;
import com.pulseinsights.surveysdk.R;
import com.pulseinsights.surveysdk.data.model.SelectOption;
import com.pulseinsights.surveysdk.data.model.SurveyTicket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeoChao on 2016/10/13.
 */

public class PartChoiceType extends RelativeLayout {

    Context context;
    View layout;
    boolean typeSingle = true;// true: single(Default), false: multiple
    int maxSelectAmount = -1;
    int selectedCount = 0;
    List<SelectOption> storeOptions = new ArrayList<>();
    SurveyFlowResult surveyFlowResult;
    LinearLayout choiceSection;
    List<View> listSurveySelectItem = new ArrayList<View>();
    String multiAnswer = "";
    String multiOptionNext = "";
    TextView countMessage;
    AnswerOnChange answerOnChange;
    SurveyTicket surveyTicket;

    public PartChoiceType(Context context) {
        super(context);
        init(context);
    }

    public PartChoiceType(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    void init(Context context) {
        this.context = context;
        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = layoutInflater.inflate(R.layout.pulse_insight_survey_choisetype, this);
        choiceSection = layout.findViewById(R.id.choice_section);
        countMessage = layout.findViewById(R.id.count_message);
        LocalData.instant.themeStyles.smallFont.configText(countMessage);
        LocalData.instant.themeStyles.helper.applyMargin(context, choiceSection);
    }

    public void setChoiceType(boolean setSingle, int maxSelectAmount) {
        typeSingle = setSingle;
        this.maxSelectAmount = maxSelectAmount;
    }

    private void selectSingleInList(int selectId) {
        for (int val = 0; val < listSurveySelectItem.size(); val++) {
            if (val != selectId) {
                AnswerButton tmpItem = (AnswerButton) listSurveySelectItem.get(val);
                tmpItem.setChecked(false);
            }
        }
    }

    private int getMultiSelectCount() {
        int rtVal = 0;
        for (int val = 0; val < listSurveySelectItem.size(); val++) {
            AnswerButton answerButton = (AnswerButton) listSurveySelectItem.get(val);
            if (answerButton.isChecked()) {
                rtVal++;
            }
        }
        return rtVal;
    }

    private void returnResult(String strAnswerString, String strNextId) {
        if (surveyFlowResult != null) {
            surveyFlowResult.result(strAnswerString, strNextId);
        }
    }

    public String getMultiAnswer() {
        multiAnswer = "";
        multiOptionNext = "";
        for (int val = 0; val < listSurveySelectItem.size(); val++) {
            AnswerButton tmpItem = (AnswerButton) listSurveySelectItem.get(val);
            if (tmpItem.isChecked()) {
                SelectOption answerData = storeOptions.get(val);
                if (!multiAnswer.isEmpty()) {
                    multiAnswer += "&";
                }
                if (val + 1 == storeOptions.size()
                        && !answerData.nextQuestionId.equalsIgnoreCase("0")
                        && multiOptionNext.isEmpty()) {
                    multiOptionNext = answerData.nextQuestionId;
                }
                multiAnswer += answerData.id;
            }
        }
        return multiAnswer;
    }

    int sectionWidth = 0;
    int colPerRow = 1;

    public void setOnChangeListener(AnswerOnChange listener) {
        answerOnChange = listener;
    }

    public void inputOptionList() {

        choiceSection.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (sectionWidth == 0) {
                            sectionWidth = MeasureTools.px2dip(context, choiceSection.getWidth());
                            layoutButtons(colPerRow > 1 || surveyTicket.nps
                                    ? sectionWidth / colPerRow : 0);
                        }
                    }
                });
    }

    private void layoutButtons(int columnWidth) {
        choiceSection.removeAllViews();
        choiceSection.removeAllViewsInLayout();
        storeOptions = new ArrayList<>();
        listSurveySelectItem = new ArrayList<View>();
        multiAnswer = "";
        multiOptionNext = "";
        LinearLayout subRow = new LinearLayout(context);
        subRow.setOrientation(LinearLayout.HORIZONTAL);
        int inRow = 0;
        for (SelectOption tmpItem : surveyTicket.possibleAnswers) {
            AnswerButton subItem = new AnswerButton(context, typeSingle, columnWidth);
            subItem.setShouldSetSelectedColor(LocalData.instant.surveyPack.survey.displayAllQuestions);
            subItem.setButtonContent(tmpItem, storeOptions.size());
            subItem.setInitVal(false, new InfCusSwitchBtn() {
                @Override
                public void changeResult(boolean status, int itemId) {
                    if (typeSingle) {
                        selectSingleInList(itemId);
                        SelectOption tmpRef = storeOptions.get(itemId);
                        returnResult(tmpRef.id, tmpRef.nextQuestionId);
                    } else {
                        selectedCount = getMultiSelectCount();
                        refreshCounterMessage();
                        if (answerOnChange != null) {
                            answerOnChange.onChange();
                        }
                    }
                }
            });
            subRow.addView(subItem);
            inRow++;
            if (inRow == colPerRow) {
                choiceSection.addView(subRow);
                subRow = new LinearLayout(context);
                subRow.setOrientation(LinearLayout.HORIZONTAL);
                inRow = 0;
            }
            listSurveySelectItem.add(subItem);

            storeOptions.add(tmpItem);
        }
        choiceSection.addView(subRow);
    }

    private void refreshCounterMessage() {
        countMessage.setVisibility(!typeSingle && (maxSelectAmount > -1)
                && (selectedCount > maxSelectAmount) ? View.VISIBLE : View.GONE);
        countMessage.setText(String.format(context.getResources()
                .getString(R.string.common_limit_counter), selectedCount, maxSelectAmount));
    }

    public boolean clearToSubmit() {
        return (selectedCount > 0) && ((maxSelectAmount == -1)
                || (selectedCount <= maxSelectAmount));
    }

    public void setCallBack(SurveyFlowResult surveyFlowResult) {
        this.surveyFlowResult = surveyFlowResult;
    }

    public void setInitialSet(SurveyTicket surveyTicket, SurveyFlowResult surveyFlowResult) {
        this.sectionWidth = 0;
        this.surveyTicket = surveyTicket;
        this.colPerRow = Math.max(1, surveyTicket.answersPerRow);
        boolean isSingleType =
                surveyTicket.questionType.equalsIgnoreCase("single_choice_question");
        setChoiceType(isSingleType,
                isSingleType ? 1 : Integer.parseInt(surveyTicket.maximumSelection));
        refreshCounterMessage();
        inputOptionList();
        setCallBack(surveyFlowResult);
    }
}
