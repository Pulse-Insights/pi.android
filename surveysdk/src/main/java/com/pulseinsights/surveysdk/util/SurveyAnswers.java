package com.pulseinsights.surveysdk.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class SurveyAnswers {
    private List<SurveyAnswer> answers;

    public SurveyAnswers() {
        this.answers = new ArrayList<>();
    }

    public void setAnswer(String questionId, String questionType, String answer) {
        if (TextUtils.isEmpty(answer)) {
            Iterator<SurveyAnswer> iterator = answers.iterator();
            while (iterator.hasNext()) {
                SurveyAnswer surveyAnswer = iterator.next();
                if (Objects.equals(surveyAnswer.getQuestionId(), questionId)) {
                    iterator.remove();
                }
            }
            return;
        }
        if (questionType.equals("single_choice_question")) {
            for (int i = answers.size() - 1; i >= 0; i--) {
                SurveyAnswer existingAnswer = answers.get(i);
                if (Objects.equals(existingAnswer.getQuestionId(), questionId)) {
                    if (Objects.equals(existingAnswer.getAnswer(), answer)) {
                        // If the questionId exists and the answer is the same, remove it
                        answers.remove(i);
                    } else {
                        // If the questionId exists but the answer is different, replace it
                        existingAnswer.setAnswer(answer);
                    }
                    return;
                }
            }
        } else if (questionType.equals("free_text_question")) {
            for (SurveyAnswer surveyAnswer : answers) {
                if (Objects.equals(surveyAnswer.getQuestionId(), questionId)) {
                    surveyAnswer.setAnswer(answer);
                    return;
                }
            }
        } else if (questionType.equals("multiple_choices_question")) {
            // need to format 001&002 => 001,002
            answer = answer.replace("&", ",");
            for (SurveyAnswer surveyAnswer : answers) {
                if (Objects.equals(surveyAnswer.getQuestionId(), questionId)) {
                    surveyAnswer.setAnswer(answer);
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
