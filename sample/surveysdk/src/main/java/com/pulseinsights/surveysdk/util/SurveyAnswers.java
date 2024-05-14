package com.pulseinsights.surveysdk.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SurveyAnswers {
    private List<SurveyAnswer> answers;

    public SurveyAnswers() {
        this.answers = new ArrayList<>();
    }

    public void setAnswer(String questionId, String questionType, String answer) {
        if (questionType.equals("single_choice_question") || questionType.equals("free_text_question")) {
            for (SurveyAnswer surveyAnswer : answers) {
                if (Objects.equals(surveyAnswer.getQuestionId(), questionId)) {
                    surveyAnswer.setAnswer(answer);
                    return;
                }
            }
        } else if (questionType.equals("multiple_choices_question")) {
            for (SurveyAnswer surveyAnswer : answers) {
                if (Objects.equals(surveyAnswer.getQuestionId(), questionId)) {
                    String existingAnswer = surveyAnswer.getAnswer();
                    if (!existingAnswer.contains(answer)) {
                        surveyAnswer.setAnswer(existingAnswer + "," + answer);
                    }
                    return;
                }
            }
        }
        answers.add(new SurveyAnswer(questionId, questionType, answer));
    }

    public List<SurveyAnswer> getAnswers() {
        return answers;
    }

    public boolean isAnswered(String questionId) {
        for (SurveyAnswer surveyAnswer : answers) {
            if (Objects.equals(surveyAnswer.getQuestionId(), questionId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return answers.isEmpty();
    }
}
