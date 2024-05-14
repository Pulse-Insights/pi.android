package com.pulseinsights.surveysdk.jsontool;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulseinsights.surveysdk.data.model.AnswerOptionData;
import com.pulseinsights.surveysdk.data.model.PollResult;
import com.pulseinsights.surveysdk.data.model.Survey;
import com.pulseinsights.surveysdk.data.model.SurveyData;
import com.pulseinsights.surveysdk.data.model.SurveyTicket;
import com.pulseinsights.surveysdk.data.model.SurveyTicketData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeoChao on 2016/10/11.
 */

public class JsonDataParser {

    public Survey parserInitialSurveyItem(String strInput) {
        try {
            SurveyData rtItem = new Gson().fromJson(strInput, new TypeToken<SurveyData>() {
            }.getType());
            return rtItem.toSurvey();
        } catch (Exception err) {
            err.printStackTrace();
            return new Survey();
        }
    }

    public PollResult parserPollResult(String input, String title) {
        PollResult rtItem = new PollResult();

        rtItem.title = title;
        rtItem.countAnswers = new ArrayList<>();

        try {
            List<AnswerOptionData> answerPolls =
                    new Gson().fromJson(input, new TypeToken<List<AnswerOptionData>>() {
                    }.getType());

            for (AnswerOptionData data : answerPolls) {
                rtItem.countAnswers.add(data.toAnswerOption());
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

        return rtItem;
    }

    public List<SurveyTicket> parserSurveyContent(String strInput) {
        List<SurveyTicket> rtItem = new ArrayList<>();

        try {
            List<SurveyTicketData> tickets =
                    new Gson().fromJson(strInput, new TypeToken<List<SurveyTicketData>>() {
                    }.getType());

            for (SurveyTicketData data : tickets) {
                rtItem.add(data.toSurveyTicket());
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return rtItem;
    }
}
