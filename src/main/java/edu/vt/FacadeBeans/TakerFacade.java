/*
 * Created by Zhengbo Wang on 2021.3.8
 * Copyright © 2021 Zhengbo Wang. All rights reserved.
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.Taker;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

// @Stateless annotation implies that the conversational state with the client shall NOT be maintained.
@Stateless
public class TakerFacade extends AbstractFacade<Taker> {

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
    public TakerFacade() {
        // Invokes super's AbstractFacade constructor method by passing
        // Recipe.class, which is the object reference of the Recipe class.
        super(Taker.class);
    }


//    public Quiz findOneQuiz(String searchQuery) {
//        if (em.createQuery("SELECT c FROM Quiz c WHERE c.accessCode = :access_code")
//                .setParameter("access_code", searchQuery)
//                .getResultList().isEmpty()) {
//            return null;
//        } else {
//            return (Quiz) (em.createQuery("SELECT c FROM Quiz c WHERE c.accessCode = :access_code")
//                    .setParameter("access_code", searchQuery)
//                    .getSingleResult());
//        }
//    }

}
