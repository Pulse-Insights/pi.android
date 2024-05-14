package com.pulseinsights.surveysdk.data.model;

import java.util.ArrayList;
import java.util.List;

public class SurveyTicket {
    public String id = "";
    public String questionType = "";
    public String position = "";
    public String content = "";
    public List<SelectOption> possibleAnswers = new ArrayList<SelectOption>();
    public String submitLabel = "";
    public String maximumSelection = "";
    public String hintText = "";
    public boolean optional = false;
    public String emptyError = "";
    public int maxTextLength = 0;
    public int answersPerRow = 1;
    public String nextQuestionId = "";
    public String fullscreen = "";
    public String backgroundColor = "";
    public String opacity = "";
    public boolean autocloseEnabled = false;
    public boolean nps = false;
    public int autocloseDelay = 0;
    public boolean autoredirectEnabled = false;
    public int autoredirectDelay = 0;
    public String autoredirectUrl = "";
    public String beforeHelper = "";
    public String afterHelper = "";
    public ArrayList<String> beforeAnswerItems = new ArrayList<>();
    public ArrayList<String> afterAnswerItems = new ArrayList<>();
    public int beforeAnswerCount = 0;
    public int afterAnswerCount = 0;
}
