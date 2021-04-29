/*
 * Created by Zhengbo Wang on 2021.3.8
 * Copyright © 2021 Zhengbo Wang. All rights reserved.
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
    Annotating 'private EntityManager em;' with '@PersistenceContext(unitName = "Recipes-WangZhengboPU")'
    implies that the EntityManager instance pointed to by 'em' is associated with the
    'Recipes-WangZhengboPU' persistence context. The persistence context is a set of entity (Recipes)
    instances in which for any persistent entity (Recipes) identity, there is a unique entity (Recipes)
    instance. Within the persistence context, the entity (Recipes) instances and their life cycle are
    managed. The EntityManager API is used to create and remove persistent entity (Recipes) instances,
    to find entities by their primary key, and to query over entities (Recipes).
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
    with the Recipe class object reference returned by the Recipe.class.
     */
    public QuestionFacade() {
        // Invokes super's AbstractFacade constructor method by passing
        // Recipe.class, which is the object reference of the Recipe class.
        super(Question.class);
    }


    public List<Question> findAllquestions(int tempQuizID) {

        List<Question> result = em.createQuery("SELECT c FROM Question c WHERE c.quizID = :quizID")
                    .setParameter("quizID", tempQuizID)
                    .getResultList();
        return result;
    }

    public List<Question> findQuestionByQuizId(int quizID) {
        List<Question> questions = em.createNamedQuery("Question.findByQuizID")
                .setParameter("quiz_id_fk", quizID)
                .getResultList();

        return questions;
    }

    public Question findQuestionByQuestionId(int questionID) {
        Question question = (Question) em.createNamedQuery("Question.findById")
                .setParameter("question_id", questionID)
                .getSingleResult();
        return question;
    }
}
