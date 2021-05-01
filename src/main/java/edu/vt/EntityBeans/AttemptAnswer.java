/*
 * Created by Osman Balci on 2021.2.10
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.EntityBeans;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

// The @Entity annotation designates this class as a JPA Entity class representing the User table in the CloudDriveDB database.
@Entity

//CREATE TABLE Answer(
//        answer_id INT PRIMARY KEY AUTO_INCREMENT,
//        question_id_fk INT,
//        answer_text VARCHAR(1024),
//        instructor_result boolean NOT NULL,
//        FOREIGN KEY (question_id_fk) REFERENCES Question (question_id)
//        );
// Name of the database table represented
@Table(name = "AttemptAnswer")

@NamedQueries({
        @NamedQuery(name = "AttemptAnswer.findAll", query = "SELECT a FROM AttemptAnswer a")
        , @NamedQuery(name = "AttemptAnswer.findById", query = "SELECT a FROM AttemptAnswer a WHERE a.id = :attemptanswer_id")
        , @NamedQuery(name = "AttemptAnswer.findByQuestionID", query = "SELECT a FROM AttemptAnswer a WHERE a.questionID = :question_id_fk")
        , @NamedQuery(name = "AttemptAnswer.findByAnswerID", query = "SELECT a FROM AttemptAnswer a WHERE a.answerID = :answer_id_fk")
        , @NamedQuery(name = "AttemptAnswer.findByAttemptID", query = "SELECT a FROM AttemptAnswer a WHERE a.attemptID = :attempt_id_fk")})

public class AttemptAnswer implements Serializable {
    /*
    ========================================================
    Instance variables representing the attributes (columns)
    of the User table in the CloudDriveDB database.
    ========================================================
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "attemptanswer_id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "question_id_fk")
    private Integer questionID;

    @Basic(optional = false)
    @NotNull
    @Column(name = "answer_id_fk")
    private Integer answerID;

    @Basic(optional = false)
    @NotNull
    @Column(name = "attempt_id_fk")
    private Integer attemptID;
    /*'~~`
    ===============================================================
    Class constructors for instantiating a User entity object to
    represent a row in the User table in the CloudDriveDB database.
    ===============================================================
     */
    public AttemptAnswer() {
    }

    public AttemptAnswer(Integer id) {
    }

    public AttemptAnswer(int questionID, int answerID, int attemptID) {
        this.questionID = questionID;
        this.answerID = answerID;
        this.attemptID = attemptID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestionID() {
        return questionID;
    }

    public void setQuestionID(Integer questionID) {
        this.questionID = questionID;
    }

    public Integer getAnswerID() {
        return answerID;
    }

    public void setAnswerID(Integer answerID) {
        this.answerID = answerID;
    }

    public Integer getAttemptID() {
        return attemptID;
    }

    public void setAttemptID(Integer attemptID) {
        this.attemptID = attemptID;
    }





    /*
    ======================================================
    Getter and Setter methods for the attributes (columns)
    of the User table in the CloudDriveDB database.
    ======================================================


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
        if (!(object instanceof AttemptAnswer)) {
            return false;
        }
        AttemptAnswer other = (AttemptAnswer) object;
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


}
