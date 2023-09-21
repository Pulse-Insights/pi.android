package com.pulseinsights.surveysdk.data.model;

import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.data.model.base.KeyInfoBase;
import com.pulseinsights.surveysdk.jsontool.ParseHelper;

public class SurveyData {
    KeyInfoBase submission = new KeyInfoBase();
    KeyInfoBase device = new KeyInfoBase();
    SurveyCoverData survey = new SurveyCoverData();

    @SerializedName("device_data")
    String deviceData = "";

    public Survey toSurvey() {
        Survey survey = new Survey();

        survey.survey = new ParseHelper<SurveyCoverData>()
                .getObj(this.survey, new SurveyCoverData()).toSurveyCover();
        survey.device =
                new ParseHelper<KeyInfoBase>().getObj(device, new KeyInfoBase()).toKeyInfo();
        survey.submission =
                new ParseHelper<KeyInfoBase>().getObj(submission, new KeyInfoBase()).toKeyInfo();
        survey.deviceData = new ParseHelper<String>().getObj(deviceData, "");

        return survey;
    }
}
