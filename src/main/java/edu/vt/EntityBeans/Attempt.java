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
@Table(name = "Attempt")

@NamedQueries({
        @NamedQuery(name = "Attempt.findAll", query = "SELECT t FROM Attempt t")
        , @NamedQuery(name = "Attempt.findById", query = "SELECT t FROM Attempt t WHERE t.id = :attempt_id")
        , @NamedQuery(name = "Attempt.findByTakerId", query = "SELECT t FROM Attempt t WHERE t.takerId = :taker_id_fk")
        , @NamedQuery(name = "Attempt.findByQuizId", query = "SELECT t FROM Attempt t WHERE t.quizID = :quiz_id_fk")
        , @NamedQuery(name = "Attempt.findByScore", query = "SELECT t FROM Attempt t WHERE t.score = :score")})

public class Attempt implements Serializable {
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
    @Column(name = "attempt_id")
    private Integer id;



    @Basic(optional = false)
    @NotNull
    @Column(name = "taker_id_fk")
    private Integer takerId;

    @Basic(optional = false)
    @NotNull
    @Column(name = "quiz_id_fk")
    private Integer quizID;

    @Basic(optional = false)
    @NotNull
    @Column(name = "score")
    private Integer score;
    /*
    ===============================================================
    Class constructors for instantiating a User entity object to
    represent a row in the User table in the CloudDriveDB database.
    ===============================================================
     */
    public Attempt() {
    }

    public Attempt(Integer id) {
    }

    public Attempt(int takerId, int quizID, int score) {
        this.takerId = takerId;
        this.quizID = quizID;
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTakerId() {
        return takerId;
    }

    public void setTakerId(Integer takerId) {
        this.takerId = takerId;
    }

    public Integer getQuizID() {
        return quizID;
    }

    public void setQuizID(Integer quizID) {
        this.quizID = quizID;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
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
        if (!(object instanceof Attempt)) {
            return false;
        }
        Attempt other = (Attempt) object;
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
