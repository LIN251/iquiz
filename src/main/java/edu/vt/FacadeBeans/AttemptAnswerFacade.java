/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.AttemptAnswer;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// @Stateless annotation implies that the conversational state with the client shall NOT be maintained.
@Stateless
public class AttemptAnswerFacade extends AbstractFacade<AttemptAnswer> {

    /*
    Annotating 'private EntityManager em;' with '@PersistenceContext(unitName = "IQuiz-PU")'
    implies that the EntityManager instance pointed to by 'em' is associated with the
    'IQuiz-PU' persistence context. The persistence context is a set of entity (AttemptAnswer)
    instances in which for any persistent entity (AttemptAnswer) identity, there is a unique entity (AttemptAnswer)
    instance. Within the persistence context, the entity (AttemptAnswer) instances and their life cycle are
    managed. The EntityManager API is used to create and remove persistent entity (AttemptAnswer) instances,
    to find entities by their primary key, and to query over entities (AttemptAnswer).
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
    with the AttemptAnswer class object reference returned by the AttemptAnswer.class.
     */
    public AttemptAnswerFacade() {
        // Invokes super's AbstractFacade constructor method by passing
        // AttemptAnswer.class, which is the object reference of the AttemptAnswer class.
        super(AttemptAnswer.class);
    }

    /*
    find attempt answers by answer id
     */
    public List<AttemptAnswer> findAttemptAnswerByAnswerId(int answerId) {

        List<AttemptAnswer> result = em.createQuery("SELECT c FROM AttemptAnswer c WHERE c.answerID = :answer_id_fk")
                .setParameter("answer_id_fk", answerId)
                .getResultList();
        return result;
    }

    /*
    find attempt answers by attempt id
     */
    public List<AttemptAnswer> findAttemptAnswerByAttemptId(int attemptId) {
        List<AttemptAnswer> attemptAnswerList = em.createNamedQuery("AttemptAnswer.findByAttemptID")
                .setParameter("attempt_id_fk", attemptId)
                .getResultList();
        return attemptAnswerList;
    }
}
