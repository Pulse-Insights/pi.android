package com.pulseinsights.surveysdk.util;

public class SurveyAnswer {
    private String question_id;
    private String question_type;
    private String answer;

    // Constructor
    public SurveyAnswer(String question_id, String question_type, String answer) {
        this.question_id = question_id;
        this.question_type = question_type;
        this.answer = answer;
    }

    // Getters and Setters
    public String getQuestionId() {
        return question_id;
    }

    public void setQuestionId(String question_id) {
        this.question_id = question_id;
    }

    public String getQuestionType() {
        return question_type;
    }

    public void setQuestionType(String question_type) {
        this.question_type = question_type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "SurveyAnswer{" +
                "question_id='" + question_id + '\'' +
                ", question_type='" + question_type + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}