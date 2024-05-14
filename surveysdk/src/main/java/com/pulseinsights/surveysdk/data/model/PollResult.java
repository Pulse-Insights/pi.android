package com.pulseinsights.surveysdk.data.model;

import com.pulseinsights.surveysdk.Define;

import java.util.ArrayList;
import java.util.List;

public class PollResult {
    public String title = "";
    public List<AnswerOption> countAnswers = new ArrayList<>();
    private int totalCount = -1;

    public int getTotalCount() {
        if (totalCount < 0) {
            totalCount = 0;
            for (AnswerOption item : countAnswers) {
                totalCount += item.count;
            }
        }
        return totalCount;
    }
}
