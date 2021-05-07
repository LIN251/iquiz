/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
package edu.vt.FacadeBeans;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * The AbstractFacade.java is an abstract Facade class providing a generic interface to the Entity Manager.
 *
 * @author Balci
 * @param <T> refers to the Class Type
 */
public abstract class AbstractFacade<T> {

    // An instance variable pointing to a class object of type T
    private Class<T> entityClass;

    /*  
    This is the constructor method called by the subclass UserFacade.java,
    UserPhotoFacade.java, and UserFileFacade.java class's constructor method
    by passing the User, UserPhoto, and UserFile class respectively as a parameter.
     */
    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /* 
    This method is overridden in UserFacade.java, UserPhotoFacade.java, or UserQuestionnaireFacade.java
    which is the concrete Facade subclass providing the actual implementation. 
     */
    protected abstract EntityManager getEntityManager();

    // Stores the newly Created User, UserPhoto or UserQuestionnaire (entity) object into the database.
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    // Stores the Edited User, UserPhoto or UserQuestionnaire (entity) object into the database.
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    // Deletes (Removes) the given User, UserPhoto or UserQuestionnaire (entity) object from the database.
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    // Finds a User, UserPhoto or UserQuestionnaire in the database by using its Primary Key (id) and returns it.
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    // Returns a list of object references of all of the User, UserPhoto or UserQuestionnaire entities in the database.
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    // Returns a List of User, UserPhoto or UserQuestionnaire objects in a range from the database. 
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    // Obtains and returns the total number of User, UserPhoto or UserQuestionnaire entities in the database.
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
