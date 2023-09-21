package com.pulseinsights.surveysdk.data.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.jsontool.ParseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SurveyTicketData {
    int id = -1;

    @SerializedName("question_type")
    String questionType = "";

    int position = -1;
    String content = "";

    @SerializedName("possible_answers")
    List<SelectOptionData> possibleAnswers = new ArrayList<SelectOptionData>();

    @SerializedName("submit_label")
    String submitLabel = "";

    @SerializedName("button_type")
    int buttonType = 1;

    @SerializedName("answers_per_row_mobile")
    int answersPerRow = 0;

    @SerializedName("maximum_selection")
    int maximumSelection = -1;

    @SerializedName("hint_text")
    String hintText = "";

    @SerializedName("max_length")
    int maxTextLength = 0;

    @SerializedName("next_question_id")
    int nextQuestionId = 0;

    String fullscreen = "";

    @SerializedName("background_color")
    String backgroundColor = null;

    String opacity = "";

    @SerializedName("autoclose_enabled")
    String autocloseEnabled = "f";

    @SerializedName("nps")
    String nps = "f";

    @SerializedName("autoclose_delay")
    int autocloseDelay = 0;

    @SerializedName("autoredirect_enabled")
    String autoredirectEnabled = "f";

    @SerializedName("autoredirect_delay")
    int autoredirectDelay = 0;

    @SerializedName("autoredirect_url")
    String autoredirectUrl = "";

    @SerializedName("before_question_text")
    String beforeHelper = "";

    @SerializedName("after_question_text")
    String afterHelper = "";

    @SerializedName("before_answers_items")
    String beforeAnswerItems = "";

    @SerializedName("after_answers_items")
    String afterAnswerItems = "";

    @SerializedName("before_answers_count")
    String beforeAnswerCount = "";

    @SerializedName("after_answers_count")
    String afterAnswerCount = "";

    public SurveyTicket toSurveyTicket() {
        SurveyTicket surveyTicket = new SurveyTicket();
        surveyTicket.id = String.valueOf(id);
        surveyTicket.questionType = new ParseHelper<String>().getObj(questionType, "");
        surveyTicket.position = String.valueOf(position);
        surveyTicket.content = new ParseHelper<String>().getObj(content, "");
        surveyTicket.possibleAnswers = new ArrayList<>();
        if (possibleAnswers != null) {
            for (SelectOptionData data : possibleAnswers) {
                surveyTicket.possibleAnswers.add(data.toSelectOption());
            }
        }
        surveyTicket.answersPerRow = answersPerRow;
        surveyTicket.submitLabel = new ParseHelper<String>().getObj(submitLabel, "");
        surveyTicket.maximumSelection = String.valueOf(maximumSelection);
        surveyTicket.hintText = new ParseHelper<String>().getObj(hintText, "");
        surveyTicket.maxTextLength = maxTextLength;
        surveyTicket.nextQuestionId = String.valueOf(nextQuestionId);
        surveyTicket.fullscreen = new ParseHelper<String>().getObj(fullscreen, "");
        surveyTicket.backgroundColor = new ParseHelper<String>().getObj(backgroundColor, "");
        surveyTicket.opacity = new ParseHelper<String>().getObj(opacity, "");
        surveyTicket.autocloseEnabled =
                autocloseEnabled != null && (autocloseEnabled.equalsIgnoreCase("t"));
        surveyTicket.nps =
                nps != null && (nps.equalsIgnoreCase("t"));
        surveyTicket.autocloseDelay = autocloseDelay;
        surveyTicket.autoredirectEnabled =
                autoredirectEnabled != null && autoredirectEnabled.equalsIgnoreCase("t");
        surveyTicket.autoredirectUrl =
                (autoredirectUrl == null) ? "" : (autoredirectUrl.startsWith("//")
                        ? "https:" + autoredirectUrl : autoredirectUrl);
        surveyTicket.beforeHelper = new ParseHelper<String>().getObj(beforeHelper, "");
        surveyTicket.afterHelper = new ParseHelper<String>().getObj(afterHelper, "");
        surveyTicket.beforeAnswerCount = !TextUtils.isEmpty(beforeAnswerCount) ? Integer.parseInt(beforeAnswerCount) : 0;
        surveyTicket.afterAnswerCount = !TextUtils.isEmpty(afterAnswerCount) ? Integer.parseInt(afterAnswerCount) : 0;
        surveyTicket.beforeAnswerItems = new ArrayList<>();
        if (!TextUtils.isEmpty(beforeAnswerItems)) {
            String[] beforeAnswerArray = new Gson().fromJson(beforeAnswerItems, String[].class);
            surveyTicket.beforeAnswerItems = new ArrayList<>(Arrays.asList(beforeAnswerArray));
        }
        surveyTicket.afterAnswerItems = new ArrayList<>();
        if (!TextUtils.isEmpty(afterAnswerItems)) {
            String[] afterAnswerArray = new Gson().fromJson(afterAnswerItems, String[].class);
            surveyTicket.afterAnswerItems = new ArrayList<>(Arrays.asList(afterAnswerArray));
        }
        return surveyTicket;
    }

}
