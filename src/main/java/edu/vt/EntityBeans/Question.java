/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
package edu.vt.EntityBeans;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

// The @Entity annotation designates this class as a JPA Entity class representing the Question table in the iquizDB database.
@Entity

// Name of the database table represented
@Table(name = "Question")

@NamedQueries({
        @NamedQuery(name = "Question.findAll", query = "SELECT q FROM Question q")
        , @NamedQuery(name = "Question.findById", query = "SELECT q FROM Question q WHERE q.id = :question_id")
        , @NamedQuery(name = "Question.findByQuizID", query = "SELECT q FROM Question q WHERE q.quizID = :quiz_id_fk")})


public class Question implements Serializable {
    /*
    ========================================================
    Instance variables representing the attributes (columns)
    of the Question table in the iquizDB database.
    ========================================================
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "question_id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "question_text")
    private String questionText;

    @Basic(optional = false)
    @NotNull
    @Column(name = "question_point")
    private int questionPoint;

    @Basic(optional = false)
    @NotNull
    @Column(name = "quiz_id_fk")
    private Integer quizID;

    @OneToMany(mappedBy = "questionId")
    private Collection<Answer> questionAnswerCollection;


    /*
    ===============================================================
    Class constructors for instantiating a User entity object to
    represent a row in the Question table in the iquizDB database.
    ===============================================================
     */
    public Question() {
    }

    public Question(Integer id) {
    }

    public Question(String questionText, Integer questionPoint, Integer quizID) {
        this.questionText = questionText;
        this.questionPoint = questionPoint;
        this.quizID = quizID;
    }

    /*
    ======================================================
    Getter and Setter methods for the attributes (columns)
    of the Question table in the iquizDB database.
    ======================================================
     */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getQuizID() {
        return quizID;
    }

    public void setQuizID(Integer quizID) {
        this.quizID = quizID;
    }

    public Collection<Answer> getQuestionAnswerCollection() {
        return questionAnswerCollection;
    }

    public void setQuestionAnswerCollection(Collection<Answer> questionAnswerCollection) {
        this.questionAnswerCollection = questionAnswerCollection;
    }

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
        if (!(object instanceof Question)) {
            return false;
        }
        Question other = (Question) object;
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
