/*
 * Created by Osman Balci on 2021.2.10
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.EntityBeans;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

// The @Entity annotation designates this class as a JPA Entity class representing the User table in the CloudDriveDB database.
@Entity

// Name of the database table represented
@Table(name = "Quiz")

@NamedQueries({
        @NamedQuery(name = "Quiz.findAll", query = "SELECT q FROM Quiz q")
        , @NamedQuery(name = "Quiz.findById", query = "SELECT q FROM Quiz q WHERE q.id = :quiz_id")
        , @NamedQuery(name = "Quiz.findByUserId", query = "SELECT q FROM Quiz q WHERE q.userID = :user_id_fk")})

public class Quiz implements Serializable {
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
    @Column(name = "quiz_id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "title")
    private String title;

    @Basic(optional = false)
    @NotNull
    @Column(name = "publish")
    private boolean publish;

    @Basic(optional = false)
    @NotNull
    @Column(name = "publish_at")
    private Date publishAt;

    @NotNull
    @Column(name = "time_limit")
    private int timeLimit;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "access_code")
    private String accessCode;


    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id_fk")
    private Integer userID;

    @OneToMany(mappedBy = "quizID")
    private Collection<Question> quizQuestionCollection;


    /*
    ===============================================================
    Class constructors for instantiating a User entity object to
    represent a row in the User table in the CloudDriveDB database.
    ===============================================================
     */
    public Quiz() {
    }

    public Quiz(Integer id) {
    }

    public Quiz(String title, boolean publish, Date publishAt, int timeLimit, int userID, String accessCode) {
        this.title = title;
        this.publish = publish;
        this.publishAt = publishAt;
        this.timeLimit = timeLimit;
        this.userID = userID;
        this.accessCode = accessCode;
    }

    /*
    ======================================================
    Getter and Setter methods for the attributes (columns)
    of the User table in the CloudDriveDB database.
    ======================================================
     */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public Date getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(Date publishAt) {
        this.publishAt = publishAt;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Collection<Question> getQuizQuestionCollection() {
        return quizQuestionCollection;
    }

    public void setQuizQuestionCollection(Collection<Question> quizQuestionCollection) {
        this.quizQuestionCollection = quizQuestionCollection;
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
     * Checks if the Quiz object identified by 'object' is the same as the Quiz object identified by 'id'
     *
     * @param object The Quiz object identified by 'object'
     * @return True if the Quiz 'object' and 'id' are the same; otherwise, return False
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Quiz)) {
            return false;
        }
        Quiz other = (Quiz) object;
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
