/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
package edu.vt.EntityBeans;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

// The @Entity annotation designates this class as a JPA Entity class representing the User table in the iquizDB database.
@Entity

//CREATE TABLE Answer(
//        answer_id INT PRIMARY KEY AUTO_INCREMENT,
//        question_id_fk INT,
//        answer_text VARCHAR(1024),
//        instructor_result boolean NOT NULL,
//        FOREIGN KEY (question_id_fk) REFERENCES Question (question_id)
//        );
// Name of the database table represented
@Table(name = "Answer")

@NamedQueries({
        @NamedQuery(name = "Answer.findAll", query = "SELECT a FROM Answer a")
        , @NamedQuery(name = "Answer.findById", query = "SELECT a FROM Answer a WHERE a.id = :answer_id")
        , @NamedQuery(name = "Answer.findByAnswerText", query = "SELECT a FROM Answer a WHERE a.answer_text = :answer_text")
        , @NamedQuery(name = "Answer.findByInstructorResult", query = "SELECT a FROM Answer a WHERE a.instructorResult = :instructor_result")
        , @NamedQuery(name = "Answer.findByQuizID", query = "SELECT a FROM Answer a WHERE a.questionId = :question_id_fk")})

public class Answer implements Serializable {
    /*
    ========================================================
    Instance variables representing the attributes (columns)
    of the Answer table in the iquizDB database.
    ========================================================
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "answer_id")
    private Integer id;

    @Basic(optional = false)
    @Size(min = 0, max = 1024)
    @Column(name = "answer_text")
    private String answer_text;

    @Basic(optional = false)
    @NotNull
    @Column(name = "instructor_result")
    private boolean instructorResult;

    @Basic(optional = false)
    @NotNull
    @Column(name = "question_id_fk")
    private Integer questionId;
    /*
    ===============================================================
    Class constructors for instantiating a Answer entity object to
    represent a row in the ANswer table in the iquizDB database.
    ===============================================================
     */
    public Answer() {
    }

    public Answer(Integer id) {
    }

    public Answer(String answer_text, boolean instructorResult, int questionID) {
        this.answer_text = answer_text;
        this.instructorResult = instructorResult;
        this.questionId = questionID;
    }

    /*
    ======================================================
    Getter and Setter methods for the attributes (columns)
    of the User table in the CloudDriveDB database.
    ======================================================
     */


    /*
    ================
    Instance Methods
    ================
     */
    /**
     * @return Generates and returns a hash code value for the object with id
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Checks if the Question object identified by 'object' is the same as the Question object identified by 'id'
     *
     * @param object The Question object identified by 'object'
     * @return True if the Question 'object' and 'id' are the same; otherwise, return False
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Answer)) {
            return false;
        }
        Answer other = (Answer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        // Convert the User object's database primary key (Integer) to String type and return it.
        return id.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnswer_text() {
        return answer_text;
    }

    public void setAnswer_text(String answer_text) {
        this.answer_text = answer_text;
    }

    public boolean isInstructorResult() {
        return instructorResult;
    }

    public void setInstructorResult(boolean instructorResult) {
        this.instructorResult = instructorResult;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }
}
