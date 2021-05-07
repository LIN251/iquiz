/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
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
    Annotating 'private EntityManager em;' with '@PersistenceContext(unitName = "IQuiz-PU")'
    implies that the EntityManager instance pointed to by 'em' is associated with the
    'IQuiz-PU' persistence context. The persistence context is a set of entity (Taker)
    instances in which for any persistent entity (Taker) identity, there is a unique entity (Taker)
    instance. Within the persistence context, the entity (Taker) instances and their life cycle are
    managed. The EntityManager API is used to create and remove persistent entity (Taker) instances,
    to find entities by their primary key, and to query over entities (Taker).
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
    with the Taker class object reference returned by the Taker.class.
     */
    public TakerFacade() {
        // Invokes super's AbstractFacade constructor method by passing
        // Taker.class, which is the object reference of the Taker class.
        super(Taker.class);
    }

    /*
    find taker by taker id
     */
    public Taker findTakerById(int id) {
        Taker taker = (Taker) em.createQuery("SELECT c FROM Taker c WHERE c.id = :taker_id")
                .setParameter("taker_id", id)
                .getSingleResult();
        return taker;
    }

}
