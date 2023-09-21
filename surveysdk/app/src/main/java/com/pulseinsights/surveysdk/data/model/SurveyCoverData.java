package com.pulseinsights.surveysdk.data.model;

import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.LocalData;
import com.pulseinsights.surveysdk.jsontool.ParseHelper;

public class SurveyCoverData {
    int id = -1;
    String invitation = "";

    @SerializedName("survey_type")
    int surveyType = 0;

    @SerializedName("sdk_widget_height")
    int widgetHeight = 0;

    @SerializedName("invitation_button")
    String invitationButton = null;

    @SerializedName("invitation_button_disabled")
    String invitationButtonDisable = "f";

    @SerializedName("thank_you")
    String thankYou = "";
    String background = null;

    @SerializedName("render_after_x_seconds_enabled")
    String enablePendingStart = "f";

    @SerializedName("render_after_x_seconds")
    int pendingStartTime = 0;

    @SerializedName("sdk_inline_target_selector")
    String inlineTrackId = "";

    @SerializedName("theme_native")
    RemoteThemeData themeNative = new RemoteThemeData();

    public SurveyCover toSurveyCover() {
        SurveyCover surveyCover = new SurveyCover();
        surveyCover.id = String.valueOf(id > -1 ? id : "");
        // For now, we have the 5 different types from the backend into the three group
        // Group A: Docked Widget ( survey_type = 0 ), Fullscreen ( survey_type = 4 )
        // Group B: Inline ( survey_type = 1 )
        // Group C: Top Bar ( survey_type = 2 ), Bottom Bar ( survey_type = 3 )
        surveyCover.surveyType = surveyType;
        if (surveyType == 4) {
            surveyCover.surveyType = 0;
        }
        if (surveyType == 3) {
            surveyCover.surveyType = 2;
        }
        surveyCover.widgetHeight = widgetHeight;
        surveyCover.invitation = new ParseHelper<String>().getObj(invitation, "");
        surveyCover.invitationButton = new ParseHelper<String>().getObj(invitationButton, "");
        String btnDisable = new ParseHelper<String>().getObj(invitationButtonDisable, "f");
        surveyCover.invitationButtonDisable = btnDisable.equalsIgnoreCase("t");
        surveyCover.thankYou = new ParseHelper<String>().getObj(thankYou, "");
        String backUrl = new ParseHelper<String>().getObj(background, "");
        surveyCover.background = backUrl.startsWith("//") ? "https:" + backUrl : backUrl;
        String pendStart = new ParseHelper<String>().getObj(enablePendingStart, "f");
        surveyCover.enablePendingStart = pendStart.equalsIgnoreCase("t");
        surveyCover.pendingStartTime = pendingStartTime;
        surveyCover.inlineTrackId = new ParseHelper<String>().getObj(inlineTrackId, "");
        LocalData.instant.themeStyles.updateAssignTheme(
                themeNative != null ? themeNative : new RemoteThemeData());
        return surveyCover;
    }
}
