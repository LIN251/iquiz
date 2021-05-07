/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.Question;
import edu.vt.EntityBeans.Quiz;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.Collection;
import java.util.List;

// @Stateless annotation implies that the conversational state with the client shall NOT be maintained.
@Stateless
public class QuestionFacade extends AbstractFacade<Question> {

    /*
    Annotating 'private EntityManager em;' with '@PersistenceContext(unitName = "IQuiz-PU")'
    implies that the EntityManager instance pointed to by 'em' is associated with the
    'IQuiz-PU' persistence context. The persistence context is a set of entity (Question)
    instances in which for any persistent entity (Question) identity, there is a unique entity (Question)
    instance. Within the persistence context, the entity (Question) instances and their life cycle are
    managed. The EntityManager API is used to create and remove persistent entity (Question) instances,
    to find entities by their primary key, and to query over entities (Question).
     */
    @PersistenceContext(unitName = "IQuiz-PU")
    private EntityManager em;

    // @Override annotation indicates that the super class AbstractFacade's
    // getEntityManager() method is overridden.
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /*
    This constructor method invokes the parent abstract class AbstractFacade.java's
    constructor method, which in turn initializes its entityClass instance variable
    with the Question class object reference returned by the Question.class.
     */
    public QuestionFacade() {
        // Invokes super's AbstractFacade constructor method by passing
        // Question.class, which is the object reference of the Question class.
        super(Question.class);
    }

    /*
    find all questions by quiz id
     */
    public List<Question> findAllquestions(int tempQuizID) {

        List<Question> result = em.createQuery("SELECT c FROM Question c WHERE c.quizID = :quizID")
                    .setParameter("quizID", tempQuizID)
                    .getResultList();
        return result;
    }

    /*
    find all questions by quiz id
     */
    public List<Question> findQuestionByQuizId(int quizID) {
        List<Question> questions = em.createNamedQuery("Question.findByQuizID")
                .setParameter("quiz_id_fk", quizID)
                .getResultList();

        return questions;
    }

    /*
    find a question by question id
     */
    public Question findQuestionByQuestionId(int questionID) {
        Question question = (Question) em.createNamedQuery("Question.findById")
                .setParameter("question_id", questionID)
                .getSingleResult();
        return question;
    }


}
