package com.pulseinsights.surveysdk.data.model;

import com.pulseinsights.surveysdk.data.model.base.OptionBase;
import com.pulseinsights.surveysdk.jsontool.ParseHelper;

public class AnswerOptionData extends OptionBase {
    String id = "";
    int count = 0;

    public AnswerOption toAnswerOption() {
        AnswerOption answerOption = new AnswerOption();
        answerOption.id = new ParseHelper<String>().getObj(id, "");
        answerOption.count = count;
        answerOption.content = new ParseHelper<String>().getObj(content, "");
        answerOption.imgUrl =
                (imgUrl == null) ? "" : (imgUrl.startsWith("//") ? "https:" + imgUrl : imgUrl);
        return answerOption;
    }
}
