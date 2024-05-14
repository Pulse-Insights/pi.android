package com.pulseinsights.surveysdk.util;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pulseinsights.surveysdk.LocalData;
import com.pulseinsights.surveysdk.R;
import com.pulseinsights.surveysdk.data.model.SelectOption;
import com.pulseinsights.surveysdk.define.base.DrawableBtnThemeBase;


/**
 * Created by LeoChao on 16/4/26.
 */
public class AnswerButton extends RelativeLayout {
    Context context;
    View layout;
    boolean isSingleType = false;
    boolean isOnStatus = false;
    TextView textLabel;
    TextView markSymbol;
    RelativeLayout markContainer;
    RelativeLayout labelContainer;
    ImageView answerImage;
    InfCusSwitchBtn infCusSwitchBtn;
    int setColorVal = Color.parseColor("#FFFFFF");
    RelativeLayout mainContainer;
    boolean isItemFunctionEnable = true;
    int horizontalWidth = 0;

    private boolean shouldSetSelectedColor = false;

    public AnswerButton(Context context, boolean isSingleType, int horizontalWidth) {
        super(context);
        this.isSingleType = isSingleType;
        this.horizontalWidth = horizontalWidth;
        init(context);

    }

    public AnswerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnswerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        this.context = context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layout = layoutInflater.inflate(R.layout.layout_cusbtn, this);
        textLabel = layout.findViewById(R.id.btn_text);
        markSymbol = layout.findViewById(R.id.mark_symbol);
        labelContainer = layout.findViewById(R.id.label_container);
        answerImage = layout.findViewById(R.id.answer_image);
        markContainer = layout.findViewById(R.id.mark_container);
        refreshCheckBox();
        LocalData.instant.themeStyles.checkMark.decorateCheckMark(markSymbol);

        mainContainer = layout.findViewById(R.id.btn_layer);

        this.setOnClickListener(clkNext);
        changeBtnStatus(isOnStatus, false);
        configDefaultStyle();
        configPadding();
        LocalData.instant.themeStyles.ansBtn.setupLayout(context, this);
        if (LocalData.instant.themeStyles.ansBtn.tabEffect) {
            this.setOnTouchListener(touchListener);
        }

    }

    private void configDefaultStyle() {
        LocalData.instant.themeStyles.mediumFont.configText(textLabel);
        String textColor = LocalData.instant.themeStyles.ansBtn.fontColor;
        if (!TextUtils.isEmpty(textColor)) {
            textLabel.setTextColor(Color.parseColor(textColor));
        }
        if (isOnStatus && horizontalWidth > 0 && !isSingleType) {
            setBackground(LocalData.instant.themeStyles.ansBtn
                    .getDrawable(LocalData.instant.themeStyles.ansBtn.perRowBackgroundColor));
        } else {
            setBackground(LocalData.instant.themeStyles.ansBtn.getDrawable());
        }
        if (isSingleType && LocalData.instant.themeStyles.radio.hide || horizontalWidth > 0) {
            textLabel.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
    }

    public void refreshCheckBox() {
        if (isSingleType && LocalData.instant.themeStyles.radio.hide) {
            markContainer.setVisibility(GONE);
        } else {
            markContainer.setVisibility(VISIBLE);
        }
        if (horizontalWidth > 0) {
            markContainer.setVisibility(GONE);
        }
        DrawableBtnThemeBase btnTheme = isSingleType
                ? LocalData.instant.themeStyles.radio : LocalData.instant.themeStyles.checkBox;
        if (isSingleType && btnTheme.margin != -100 && horizontalWidth == 0 && mainContainer != null) {
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.bottomMargin = MeasureTools.dip2px(context, btnTheme.margin);
            mainContainer.setLayoutParams(params);
        }
        btnTheme.setupDrawable(context, markContainer, isOnStatus);
    }

    public void configPadding() {
        int padding = LocalData.instant.themeStyles.ansBtn.padding;
        int paddingPx = MeasureTools.dip2px(context, padding);
        int paddingHorizontal = LocalData.instant.themeStyles.ansBtn.paddingHorizontal;
        int pxPaddingHorizontal = paddingHorizontal > -1 ? MeasureTools.dip2px(context, paddingHorizontal) : paddingPx;
        int paddingVertical = LocalData.instant.themeStyles.ansBtn.paddingVertical;
        int pxPaddingVertical = paddingVertical > -1 ? MeasureTools.dip2px(context, paddingVertical) : paddingPx;
        this.setPadding(pxPaddingHorizontal, pxPaddingVertical, pxPaddingHorizontal, pxPaddingVertical);
        markContainer.setPadding(0, 0, paddingPx, 0);
    }

    public void setInitVal(boolean setChecked,
                           InfCusSwitchBtn callBack) {
        setChecked(setChecked);
        setCallBack(callBack);
    }

    public void setButtonContent(SelectOption selectOptionItem, int setId) {
        this.setId(setId);
        String imgUrl = selectOptionItem.imgUrl;
        boolean useImage = !imgUrl.isEmpty();
        textLabel.setVisibility(useImage ? View.GONE : View.VISIBLE);
        answerImage.setVisibility(useImage ? View.VISIBLE : View.GONE);
        if (horizontalWidth > 0) {
            int margin = LocalData.instant.themeStyles.ansBtn.margin;
            LocalData.instant.themeStyles.ansBtn.configWidthHeight(context, this,
                    horizontalWidth - margin * 2,
                    LocalData.instant.themeStyles.ansBtn.height);
            textLabel.setMaxLines(1);
        }
        if (useImage) {
            LocalData.instant.themeStyles.ansImg.configImageView(context, answerImage, imgUrl);
            LocalData.instant.themeStyles.ansImg.configImageContainer(labelContainer);
        } else {
            textLabel.setText(
                    Html.fromHtml(FormatSetTool.transferToHtmlFormat(selectOptionItem.content)));
        }
    }

    public void setShouldSetSelectedColor(boolean shouldSetSelectedColor) {
        this.shouldSetSelectedColor = shouldSetSelectedColor;
    }

    public void setItemEnable(boolean setEnable) {
        isItemFunctionEnable = setEnable;
    }

    public void setChecked(boolean isOn) {
        changeBtnStatus(isOn, false);
    }

    public boolean isChecked() {
        return isOnStatus;
    }

    public void setCallBack(InfCusSwitchBtn infCusSwitchBtn) {
        this.infCusSwitchBtn = infCusSwitchBtn;
    }

    public void setColor(int setVal) {
        setColorVal = setVal;
        changeBtnStatus(isOnStatus, false);
    }

    void changeBtnStatus(boolean isOn, boolean needCallBack) {
        isOnStatus = isOn;
        markSymbol.setVisibility((isSingleType || !isOn) ? View.INVISIBLE : View.VISIBLE);
        refreshCheckBox();

        if (shouldSetSelectedColor) {
            if (isOnStatus) {
                // Set the selected background color when the button is selected
                this.setBackground(LocalData.instant.themeStyles.ansBtn.getDrawable(LocalData.instant.themeStyles.ansBtn.selectedBackgroundColor));
                mainContainer.setBackgroundColor(Color.parseColor(LocalData.instant.themeStyles.ansBtn.selectedBackgroundColor));
            } else {
                // Set the default background color when the button is not selected
                this.setBackground(LocalData.instant.themeStyles.ansBtn.getDrawable());
                mainContainer.setBackgroundColor(Color.parseColor(LocalData.instant.themeStyles.ansBtn.backgroundColor));
            }
        }

        if (needCallBack && infCusSwitchBtn != null) {
            infCusSwitchBtn.changeResult(isOnStatus, this.getId());
        }
    }

    OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            int action = event.getActionMasked();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    setBackground(LocalData.instant.themeStyles.ansBtn.getPressInDrawable());
                    LocalData.instant.themeStyles.ansBtn.configTextPressInColor(textLabel);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    configDefaultStyle();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    OnClickListener clkNext = new OnClickListener() {

        @Override
        public void onClick(View view) {

            if (isItemFunctionEnable) {
                isOnStatus = !isOnStatus;
                changeBtnStatus(isOnStatus, true);
                if (!shouldSetSelectedColor) {
                    configDefaultStyle();
                }
            }
        }
    };

}
