package edu.vt.pojo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

public class AnswerChoice implements Serializable {
    /*
    ========================================================
    Instance variables representing the attributes (columns)
    of the User table in the CloudDriveDB database.
    ========================================================
     */
    private String answerText;
    private Boolean correct;
    private String index;
    private int belongsTo;
    private int AnswerChoiceID;
    /*
    ===============================================================
    Class constructors for instantiating a User entity object to
    represent a row in the User table in the CloudDriveDB database.
    ===============================================================
     */



    public AnswerChoice(String text, Boolean correct, String index, int belongsTo,int AnswerChoiceID) {
        this.answerText = text;
        this.correct = correct;
        this.index = index;
        this.belongsTo = belongsTo;
        this.AnswerChoiceID = AnswerChoiceID;
    }

    /*
    ======================================================
    Getter and Setter methods for the attributes (columns)
    of the User table in the CloudDriveDB database.
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
}
