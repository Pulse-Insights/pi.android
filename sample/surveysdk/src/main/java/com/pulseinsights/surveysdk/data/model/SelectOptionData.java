package com.pulseinsights.surveysdk.data.model;

import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.data.model.base.OptionBase;
import com.pulseinsights.surveysdk.jsontool.ParseHelper;

public class SelectOptionData extends OptionBase {
    int id = -1;

    @SerializedName("next_question_id")
    int nextQuestionId = -1;

    SelectOption toSelectOption() {
        SelectOption selectOption = new SelectOption();
        selectOption.id = String.valueOf(id);
        selectOption.nextQuestionId = String.valueOf(nextQuestionId);
        selectOption.content = new ParseHelper<String>().getObj(content, "");
        selectOption.imgUrl =
                (imgUrl == null) ? "" : (imgUrl.startsWith("//") ? "https:" + imgUrl : imgUrl);
        return selectOption;
    }
}
