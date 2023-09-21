package com.pulseinsights.surveysdk.define;

public class PollBarTheme {
    public boolean displayAbs = false;
    public boolean displayPercentage = true;
    public String barColor = "#676767";

    public void applyNewStyle(PollBarTheme newStyle) {
        this.displayAbs = newStyle.displayAbs;
        this.displayPercentage = newStyle.displayPercentage;
        this.barColor = newStyle.barColor;

    }

    public void resetStyle() {
        this.applyNewStyle(new PollBarTheme());
    }

}