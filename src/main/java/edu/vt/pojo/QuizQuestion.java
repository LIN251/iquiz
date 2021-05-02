package edu.vt.pojo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

public class QuizQuestion implements Serializable {
    /*
    ========================================================
    Instance variables representing the attributes (columns)
    of the User table in the CloudDriveDB database.
    ========================================================
     */
    private Integer questionId;
    private String questionText;
    private int questionPoint;
    private int questionNumber;
    private List<AnswerChoice> answerChoices;
    /*
    ===============================================================
    Class constructors for instantiating a User entity object to
    represent a row in the User table in the CloudDriveDB database.
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
    Getter and Setter methods for the attributes (columns)
    of the User table in the CloudDriveDB database.
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
