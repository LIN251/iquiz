/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
package edu.vt.pojo;

import java.io.Serializable;
import java.util.List;

public class QuizQuestion implements Serializable {
    /*
    ========================================================
    Instance variables representing the attributes
    ========================================================
     */
    private Integer questionId;
    private String questionText;
    private int questionPoint;
    private int questionNumber;
    private List<AnswerChoice> answerChoices;
    /*
    ===============================================================
    Class constructors
    ===============================================================
     */
    public QuizQuestion(Integer questionId, String questionText, Integer questionPoint, Integer questionNumber, List<AnswerChoice> answers) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.questionPoint = questionPoint;
        this.questionNumber = questionNumber;
        this.answerChoices = answers;
    }

    /*
    ======================================================
    Getter and Setter methods
    ======================================================
     */
    public Integer getQuestionId() { return questionId; }

    public void setQuestionId(Integer questionId) { this.questionId = questionId; }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getQuestionPoint() {
        return questionPoint;
    }

    public void setQuestionPoint(int questionPoint) {
        this.questionPoint = questionPoint;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public List<AnswerChoice> getAnswerChoices() {
        return answerChoices;
    }

    public void setAnswerChoices(List<AnswerChoice> answerChoices) {
        this.answerChoices = answerChoices;
    }
}
