package com.pulseinsights.surveysdk.util;

import android.text.TextUtils;

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
            if (TextUtils.isEmpty(answer)) {
                // remove answer if it is empty with it's questionId
                answers.removeIf(surveyAnswer -> Objects.equals(surveyAnswer.getQuestionId(), questionId));
                return;
            }
            for (SurveyAnswer surveyAnswer : answers) {
                if (Objects.equals(surveyAnswer.getQuestionId(), questionId)) {
                    surveyAnswer.setAnswer(answer);
                    return;
                }
            }
        } else if (questionType.equals("multiple_choices_question")) {
            if (TextUtils.isEmpty(answer)) {
                // remove answer if it is empty with it's questionId
                answers.removeIf(surveyAnswer -> Objects.equals(surveyAnswer.getQuestionId(), questionId));
                return;
            }
            // need to format 001&002 => 001,002
            answer = answer.replace("&", ",");
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isEmpty()) {
            sb.append("SurveyAnswers: Empty");
        } else {
            sb.append("SurveyAnswers: ");
            for (SurveyAnswer answer : answers) {
                sb.append("\n").append(answer.toString());
            }
        }
        return sb.toString();
    }
}
