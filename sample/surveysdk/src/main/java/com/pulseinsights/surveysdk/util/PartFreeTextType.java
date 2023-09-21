package com.pulseinsights.surveysdk.util;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pulseinsights.surveysdk.LocalData;
import com.pulseinsights.surveysdk.R;

/**
 * Created by LeoChao on 2016/10/13.
 */

public class PartFreeTextType extends RelativeLayout {
    Context context;
    View view;
    EditText inputTextSection;
    RelativeLayout inputTextSectionBase;
    AnswerOnChange answerOnChange;
    TextView counterText;
    int maxTextLength = 0;
    int inputSize = 0;

    public PartFreeTextType(Context context) {
        super(context);
        init(context);
    }

    public PartFreeTextType(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public boolean clearToSubmit() {
        return (maxTextLength - inputSize) >= 0;
    }

    void init(Context context) {
        this.context = context;
        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.pulse_insight_survey_texttype, this);
        counterText = (TextView) view.findViewById(R.id.counter_text);
        LocalData.instant.themeStyles.smallFont.configText(counterText);
        inputTextSection = (EditText) view.findViewById(R.id.inputtext_section);
        inputTextSection.setTextColor(
                Color.parseColor(LocalData.instant.themeStyles.freeText.fontColor));
        inputTextSection.setTextSize(LocalData.instant.themeStyles.smallFont.fontSize);
        inputTextSection.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence sequence, int start, int before, int count) {
                inputSize = sequence.length();
                formatCountString();
                if (answerOnChange != null) {
                    answerOnChange.onChange();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputTextSectionBase = (RelativeLayout) view.findViewById(R.id.inputtext_section_base);
        inputTextSectionBase.setBackground(
                LocalData.instant.themeStyles.freeText.getBackgroundDrawable());
        formatCountString();

    }

    public void setOnChangeListener(AnswerOnChange listener) {
        answerOnChange = listener;
    }

    public void setMaxTextLength(int maxLength) {
        maxTextLength = maxLength;
        inputTextSection.setFilters(
                new InputFilter[]{new InputFilter.LengthFilter(Math.max(maxTextLength, 0))});
        formatCountString();
    }

    private void formatCountString() {
        int inputTextCount = inputSize;
        counterText.setText(String.format(context.getResources()
                .getString(R.string.common_limit_counter), inputTextCount, maxTextLength));
    }

    public String getInputAnswer() {
        return inputTextSection.getText().toString();
    }

    private void initUiParts() {
        inputTextSection.setHint("");
        inputTextSection.setText("");
    }

    public void setHintText(String strSetText) {
        initUiParts();
        //2016-12-07
        //Although no such behavior in javascript version,
        // keep the statement in case we need in the future
        //2016-12-12
        //Confirmed that we should applied Html format in every where of this survey
        inputTextSection.setHint(Html.fromHtml(FormatSetTool.transferToHtmlFormat(strSetText)));
        inputTextSection.setHintTextColor(
                Color.parseColor(LocalData.instant.themeStyles.freeText.placeholderFontColor));
        //inputTextSection.setHint(strSetText);
    }
}
