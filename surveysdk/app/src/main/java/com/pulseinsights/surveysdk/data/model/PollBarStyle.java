package com.pulseinsights.surveysdk.data.model;

import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.define.PollBarTheme;
import com.pulseinsights.surveysdk.jsontool.ParseHelper;

public class PollBarStyle {
    @SerializedName("bar-color")
    String barColor = null;

    @SerializedName("display-abs")
    boolean displayAbs = new PollBarTheme().displayAbs;

    @SerializedName("display-percentage")
    boolean displayPercentage = new PollBarTheme().displayPercentage;

    public PollBarTheme toTheme() {
        PollBarTheme pollBarTheme = new PollBarTheme();
        pollBarTheme.barColor =
                new ParseHelper<String>().getObj(barColor, pollBarTheme.barColor);
        pollBarTheme.displayAbs = displayAbs;
        pollBarTheme.displayPercentage = displayPercentage;
        return pollBarTheme;
    }
}
