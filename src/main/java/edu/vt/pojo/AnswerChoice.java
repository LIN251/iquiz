/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
package edu.vt.pojo;


import java.io.Serializable;


public class AnswerChoice implements Serializable {
    /*
    ========================================================
    Instance variables representing the attributes
    ========================================================
     */
    private String answerText;
    private Boolean correct;
    private String index;
    private int belongsTo;
    private int AnswerChoiceID;
    private Boolean studentCorrect;
    /*
    ===============================================================
    Class constructors
    ===============================================================
     */
    public AnswerChoice(String text, Boolean correct, String index, int belongsTo,int AnswerChoiceID, Boolean studentCorrect) {
        this.answerText = text;
        this.correct = correct;
        this.index = index;
        this.belongsTo = belongsTo;
        this.AnswerChoiceID = AnswerChoiceID;
        this.studentCorrect = studentCorrect;
    }

    /*
    ======================================================
    Getter and Setter methods
    ======================================================
     */

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public int getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(int belongsTo) {
        this.belongsTo = belongsTo;
    }

    public int getAnswerChoiceID() {
        return AnswerChoiceID;
    }

    public void setAnswerChoiceID(int answerChoiceID) {
        AnswerChoiceID = answerChoiceID;
    }

    public Boolean getStudentCorrect() {
        return studentCorrect;
    }

    public void setStudentCorrect(Boolean studentCorrect) {
        this.studentCorrect = studentCorrect;
    }
}
