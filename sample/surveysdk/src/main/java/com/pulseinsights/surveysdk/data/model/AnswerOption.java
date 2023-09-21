package com.pulseinsights.surveysdk.data.model;

public class AnswerOption {
    public String id = "";
    public String content = "";
    public String imgUrl = "";
    public int count = 0;

    public int getPercentage(int totalCount) {
        if (totalCount <= 0) {
            return 0;
        } else {
            return count * 100 / totalCount;
        }
    }
}
