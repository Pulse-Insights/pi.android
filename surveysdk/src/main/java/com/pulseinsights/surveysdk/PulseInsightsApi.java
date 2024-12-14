package com.pulseinsights.surveysdk;

import android.content.Context;

import com.google.gson.Gson;
import com.pulseinsights.surveysdk.data.model.PollResult;
import com.pulseinsights.surveysdk.data.model.SurveyTicket;
import com.pulseinsights.surveysdk.jsontool.HttpCore;
import com.pulseinsights.surveysdk.jsontool.JsonDataParser;
import com.pulseinsights.surveysdk.jsontool.JsonGetResult;
import com.pulseinsights.surveysdk.util.DebugTool;
import com.pulseinsights.surveysdk.util.EventListener;
import com.pulseinsights.surveysdk.util.PreferencesManager;
import com.pulseinsights.surveysdk.util.SurveyAnswer;
import com.pulseinsights.surveysdk.util.SurveyAnswers;
import com.pulseinsights.surveysdk.util.SurveyFlowResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PulseInsightsApi {

    private Context context;
    private HttpCore httpCore = null;
    private SurveyFlowResult surveyFlowResult = null;

    public PulseInsightsApi(Context context) {
        initialItem(context);
    }

    public PulseInsightsApi(Context context, SurveyFlowResult surveyFlowResult) {
        initialItem(context);
        setCallBack(surveyFlowResult);
    }

    private void initialItem(Context context) {
        this.context = context;
        if (httpCore == null) {
            httpCore = new HttpCore(this.context);
        }
        httpCore.setCallBack(jsonGetResult);
    }

    public void setCallBack(SurveyFlowResult callBack) {
        surveyFlowResult = callBack;
    }

    private void sendFeedBack(String strType, String strExtend) {
        if (surveyFlowResult != null) {
            surveyFlowResult.result(strType, strExtend);
        }
    }

    private Map<String, String> serveStandParamsMap() {
        Map<String, String> requestParams = new HashMap<>();

        requestParams.put("udid", LocalData.instant.strUdid);
        requestParams.put("device_type", LocalData.instant.strDeviceType);
        requestParams.put("mobile_type", LocalData.instant.strMobileType);
        requestParams.put("identifier", LocalData.instant.strAccountId);
        requestParams.put("install_days", String.valueOf(LocalData.instant.installDays));
        requestParams.put("launch_times",
                String.valueOf(PreferencesManager.getInstance(context).getLaunchCount()));
        requestParams.put("view_name", LocalData.instant.strViewName);
        return requestParams;
    }

    public void serve() {
        Map<String, String> customData = LocalData.instant.customData;
        Map<String, String> requestParams = serveStandParamsMap();
        requestParams.put("preview_mode", String.valueOf(LocalData.instant.previewMode));
        String clientKey = PreferencesManager.getInstance(context).getClientKey();
        if (!clientKey.isEmpty()) {
            requestParams.put("client_key", clientKey);
        }
        if (customData.size() > 0) {
            requestParams.put("custom_data", HttpCore.stringifyMap(customData));
        }
        String strRequestUrl = httpCore.composeRequestUrl("serve", requestParams);
        httpCore.startRequest(strRequestUrl, Define.SURVEY_REQ_TYPE_SCAN);
    }

    public void setDeviceData(Map<String, String> map) {
        HttpCore core = new HttpCore(this.context);
        core.setCallBack(new JsonGetResult() {
            @Override
            public void getResult(String strRes, String strExtend) {

            }
        });
        Map<String, String> attributes = map == null ? new HashMap<String, String>() : map;
        attributes.put("identifier", LocalData.instant.strAccountId);
        String path = String.format("devices/%s/set_data", LocalData.instant.strUdid);
        String strRequestUrl =
                httpCore.composeRequestUrl(path, attributes);
        httpCore.startRequest(strRequestUrl, "set_data");
    }

    public void getSurveyInformation() {
        String domainSuffix = String.format("surveys/%s", LocalData.instant.strCheckingSurveyId);
        String strRequestUrl = httpCore.composeRequestUrl(domainSuffix, serveStandParamsMap());
        httpCore.startRequest(strRequestUrl, Define.SURVEY_REQ_TYPE_PRESENT);
    }

    public void viewedAt() {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("identifier", LocalData.instant.strAccountId);

        // Add the current date-time as the viewed_at parameter
        String viewedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS Z").format(new Date());
        requestParams.put("viewed_at", viewedAt);

        // Construct the URL for the HTTP request
        String domainSuffix = String.format("submissions/%s/viewed_at", LocalData.instant.strSubmitId);
        String strRequestUrl = httpCore.composeRequestUrl(domainSuffix, requestParams);

        // Start the HTTP request
        httpCore.startRequest(strRequestUrl, Define.SURVEY_REQ_VIEWED_AT);
    }

    private boolean shouldParsePoll = false;
    private String readyPollTitle = "";
    private EventListener callback = null;

    public void postAnswers(String strPostAnswers, String strQuestionId,
                            String strQuestionType, EventListener callback) {

        this.callback = callback;
        String strQidTagName = "answer_id";
        checkPollParserElement(strQuestionId, strQuestionType);
        if (strQuestionType.equalsIgnoreCase("free_text_question")) {
            strQidTagName = "text_answer";
        } else if (strQuestionType.equalsIgnoreCase("single_choice_question")) {
            strQidTagName = "answer_id";
        } else if (strQuestionType.equalsIgnoreCase("multiple_choices_question")) {
            strQidTagName = "check_boxes";
        }

        Map<String, String> requestParams = new HashMap<>();

        requestParams.put("identifier", LocalData.instant.strAccountId);
        requestParams.put("question_id", strQuestionId);
        requestParams.put(strQidTagName, strPostAnswers);

        String domainSuffix = String.format("submissions/%s/answer", LocalData.instant.strSubmitId);

        String strRequestUrl = httpCore.composeRequestUrl(domainSuffix, requestParams);

        httpCore.startRequest(strRequestUrl, Define.SURVEY_REQ_TYPE_SENDANSWER);
    }

    public void postAllAtOnce(SurveyAnswers answers, EventListener callback) {
        this.callback = callback;


        // Convert the answers to a JSON string
        Gson gson = new Gson();
        String jsonAnswers = gson.toJson(answers.getAnswers());
        // Prepare the request parameters
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("identifier", LocalData.instant.strAccountId);
        requestParams.put("answers", jsonAnswers);
        // Construct the URL for the HTTP request
        String domainSuffix = String.format("submissions/%s/all_answers", LocalData.instant.strSubmitId);
        String strRequestUrl = httpCore.composeRequestUrl(domainSuffix, requestParams);

        // Start the HTTP request
        httpCore.startRequest(strRequestUrl, Define.SURVEY_REQ_TYPE_SENDANSWER);
    }

    private void checkPollParserElement(String strQuestionId, String strQuestionType) {
        readyPollTitle = "";
        shouldParsePoll = false;
        if (strQuestionType.equalsIgnoreCase("single_choice_question")) {
            shouldParsePoll = true;
            readyPollTitle = getTitleById(strQuestionId);
        } else if (strQuestionType.equalsIgnoreCase("multiple_choices_question")) {
            shouldParsePoll = true;
            readyPollTitle = getTitleById(strQuestionId);
        }
    }

    private String getTitleById(String questionId) {
        String rtTitle = "";

        for (SurveyTicket item : LocalData.instant.surveyTickets) {
            if (questionId.equalsIgnoreCase(item.id)) {
                rtTitle = item.content;
                break;
            }
        }

        return rtTitle;
    }

    public void postClose() {
        Map<String, String> requestParams = new HashMap<>();
        String domainSuffix = String.format("submissions/%s/close", LocalData.instant.strSubmitId);
        String strRequestUrl = httpCore.composeRequestUrl(domainSuffix, requestParams);
        httpCore.startRequest(strRequestUrl, Define.SURVEY_REQ_TYPE_CLOSE);
    }

    public void getQuestionDetail() {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("identifier", LocalData.instant.strAccountId);
        String domainSuffix =
                String.format("surveys/%s/questions", LocalData.instant.strCheckingSurveyId);
        String strRequestUrl = httpCore.composeRequestUrl(domainSuffix, requestParams);
        httpCore.startRequest(strRequestUrl, Define.SURVEY_REQ_TYPE_GETCONTENT);
    }

    JsonGetResult jsonGetResult = new JsonGetResult() {

        @Override
        public void getResult(String strRes, String strExtend) {
            if (!strExtend.equalsIgnoreCase("-1")) {
                String strResProcess = "";
                int firstMatch = strRes.indexOf('(');
                int lastMatch = strRes.lastIndexOf(')');
                int lastIndex = strRes.length() - 1;
                DebugTool.debugPrintln(context, "firstMatch of (", String.valueOf(firstMatch));
                DebugTool.debugPrintln(context, "lastMatch of )", String.valueOf(lastMatch));
                DebugTool.debugPrintln(context,
                        "lastIndex of all string", String.valueOf(lastIndex));
                if ((firstMatch == 0) && (lastMatch == lastIndex - 1)) {
                    strResProcess = strRes.substring(1, strRes.length() - 2);
                } else {
                    strResProcess = strRes;
                }
                DebugTool.debugPrintln(context, strResProcess);
                JsonDataParser jsonGetResult = new JsonDataParser();


                if (strExtend.equalsIgnoreCase(Define.SURVEY_REQ_TYPE_SCAN)) {
                    LocalData.instant.surveyPack =
                            jsonGetResult.parserInitialSurveyItem(strResProcess);
                    LocalData.instant.strSubmitId = LocalData.instant.surveyPack.submission.udid;
                    if (!LocalData.instant.surveyPack.survey.id.isEmpty()) {
                        LocalData.instant.strCheckingSurveyId =
                                LocalData.instant.surveyPack.survey.id;
                    }
                    DebugTool.debugPrintln(context, "Scan Completed!");
                } else if (strExtend.equalsIgnoreCase(Define.SURVEY_REQ_TYPE_PRESENT)) {
                    LocalData.instant.surveyPack =
                            jsonGetResult.parserInitialSurveyItem(strResProcess);
                    LocalData.instant.strSubmitId = LocalData.instant.surveyPack.submission.udid;
                } else if (strExtend.equalsIgnoreCase(Define.SURVEY_REQ_TYPE_GETCONTENT)) {
                    LocalData.instant.pollResults = new ArrayList<>();
                    LocalData.instant.surveyTickets = new ArrayList<>();
                    List<SurveyTicket> parseTickets =
                            jsonGetResult.parserSurveyContent(strResProcess);
                    for (SurveyTicket ticket : parseTickets) {
                        LocalData.instant.surveyTickets.add(ticket);
                    }
                } else if (strExtend.equalsIgnoreCase(Define.SURVEY_REQ_TYPE_SENDANSWER)) {
                    if (shouldParsePoll) {
                        PollResult tmpItem =
                                jsonGetResult.parserPollResult(strResProcess, readyPollTitle);
                        if (tmpItem.countAnswers.size() > 0) {
                            LocalData.instant.pollResults.add(tmpItem);
                        }
                    }
                    //TODO: SURVEY_REQ_TYPE_SENDANSWER
                } else if (strExtend.equalsIgnoreCase(Define.SURVEY_REQ_TYPE_CLOSE)) {
                    //TODO: SURVEY_REQ_TYPE_CLOSE
                } else if (strExtend.equalsIgnoreCase(Define.SURVEY_REQ_VIEWED_AT)) {
                    DebugTool.debugPrintln(context, "Viewed at request succeed!");
                }
                sendFeedBack(strExtend, "sucess");
            } else {
                sendFeedBack("error", "");
            }
            if (callback != null) {
                callback.onEvent(true);
            }
            DebugTool.debugPrintln(context, "Done!");

        }
    };

}
