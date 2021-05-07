/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.Answer;
import edu.vt.EntityBeans.Question;
import edu.vt.EntityBeans.Quiz;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// @Stateless annotation implies that the conversational state with the client shall NOT be maintained.
@Stateless
public class AnswerFacade extends AbstractFacade<Answer> {

    /*
    Annotating 'private EntityManager em;' with '@PersistenceContext(unitName = "IQuiz-PU")'
    implies that the EntityManager instance pointed to by 'em' is associated with the
    'IQuiz-PU' persistence context. The persistence context is a set of entity (Answer)
    instances in which for any persistent entity (Answer) identity, there is a unique entity (Answer)
    instance. Within the persistence context, the entity (Answer) instances and their life cycle are
    managed. The EntityManager API is used to create and remove persistent entity (Answer) instances,
    to find entities by their primary key, and to query over entities (Answer).
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
    with the Answer class object reference returned by the Answer.class.
     */
    public AnswerFacade() {
        // Invokes super's AbstractFacade constructor method by passing
        // Answer.class, which is the object reference of the Answer class.
        super(Answer.class);
    }


    /*
    find all answers by a question id
     */
    public  List<Answer> findAllAnswersForOneQuestion(int questionId) {

        List<Answer> result = em.createQuery("SELECT c FROM Answer c WHERE c.questionId = :questionId")
                    .setParameter("questionId", questionId)
                    .getResultList();
        return result;
    }

    /*
    find question id by answer id
     */
    public int findQuestionIDByAnswerID(int answerID) {
        Answer answer = (Answer) em.createNamedQuery("Answer.findById")
                .setParameter("answer_id", answerID)
                .getSingleResult();
        return answer.getQuestionId();
    }

    /*
    find answer by answer id
     */
    public Answer findAnswerByAnswerID(int answerID) {
        Answer answer = (Answer) em.createNamedQuery("Answer.findById")
                .setParameter("answer_id", answerID)
                .getSingleResult();
        return answer;
    }

}
