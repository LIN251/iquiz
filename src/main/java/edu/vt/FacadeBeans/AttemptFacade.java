/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.Answer;
import edu.vt.EntityBeans.Attempt;
import edu.vt.EntityBeans.AttemptAnswer;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// @Stateless annotation implies that the conversational state with the client shall NOT be maintained.
@Stateless
public class AttemptFacade extends AbstractFacade<Attempt> {

    /*
    Annotating 'private EntityManager em;' with '@PersistenceContext(unitName = "IQuiz-PU")'
    implies that the EntityManager instance pointed to by 'em' is associated with the
    'IQuiz-PU' persistence context. The persistence context is a set of entity (Attempt)
    instances in which for any persistent entity (Attempt) identity, there is a unique entity (iquiz)
    instance. Within the persistence context, the entity (Attempt) instances and their life cycle are
    managed. The EntityManager API is used to create and remove persistent entity (Attempt) instances,
    to find entities by their primary key, and to query over entities (Attempt).
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
    with the Attempt class object reference returned by the Attempt.class.
     */
    public AttemptFacade() {
        // Invokes super's AbstractFacade constructor method by passing
        // iquiz.class, which is the object reference of the iquiz class.
        super(Attempt.class);
    }

    /*
    find all attempts by quiz id
     */
    public List<Attempt> findAllAttemptByQuizId(int quizId) {
        List<Attempt> attempts = em.createNamedQuery("Attempt.findByQuizId")
                .setParameter("quiz_id_fk", quizId)
                .getResultList();
        return attempts;
    }

    /*
    find all attempt by taker id
     */
    public List<Attempt> findAttemptByTakerId(int takerId) {
        List<Attempt> attempt = em.createNamedQuery("Attempt.findByTakerId")
                .setParameter("taker_id_fk", takerId)
                .getResultList();
        return attempt;
    }

}
