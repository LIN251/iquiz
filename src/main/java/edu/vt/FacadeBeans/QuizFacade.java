/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.Quiz;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// @Stateless annotation implies that the conversational state with the client shall NOT be maintained.
@Stateless
public class QuizFacade extends AbstractFacade<Quiz> {

    /*
    Annotating 'private EntityManager em;' with '@PersistenceContext(unitName = "IQuiz-PU")'
    implies that the EntityManager instance pointed to by 'em' is associated with the
    'IQuiz-PU' persistence context. The persistence context is a set of entity (Quiz)
    instances in which for any persistent entity (Quiz) identity, there is a unique entity (Quiz)
    instance. Within the persistence context, the entity (Quiz) instances and their life cycle are
    managed. The EntityManager API is used to create and remove persistent entity (Quiz) instances,
    to find entities by their primary key, and to query over entities (Quiz).
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
    with the Quiz class object reference returned by the Quiz.class.
     */
    public QuizFacade() {
        // Invokes super's AbstractFacade constructor method by passing
        // Quiz.class, which is the object reference of the Quiz class.
        super(Quiz.class);
    }

    /*
    find a quiz by a string
     */
    public Quiz findOneQuiz(String searchQuery) {
        if (em.createQuery("SELECT c FROM Quiz c WHERE c.accessCode = :access_code")
                .setParameter("access_code", searchQuery)
                .getResultList().isEmpty()) {
            return null;
        } else {
            return (Quiz) (em.createQuery("SELECT c FROM Quiz c WHERE c.accessCode = :access_code")
                    .setParameter("access_code", searchQuery)
                    .getSingleResult());
        }
    }

    /*
    find a quiz by a quiz id
     */
    public Quiz findQuizByID(int quizid) {
        Quiz quiz = (Quiz) em.createNamedQuery("Quiz.findById")
                .setParameter("quiz_id", quizid)
                .getSingleResult();
        return quiz;
    }

    /*
    find a quiz by user id
     */
    public List<Quiz> findQuizByUserId(int userId) {
        List<Quiz> result = em.createQuery("SELECT c FROM Quiz c WHERE c.userID = :user_id_fk")
                .setParameter("user_id_fk", userId)
                .getResultList();
        return result;
    }

}
