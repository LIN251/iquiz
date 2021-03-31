/*
 * Created by Calvin Huang on 2021.3.24
 * Copyright © 2021 Calvin Huang. All rights reserved.
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.UserPhoto;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class UserPhotoFacade extends AbstractFacade<UserPhoto> {

    @PersistenceContext(unitName = "IQuiz-PU")
    private EntityManager em;

    // @Override annotation indicates that the super class AbstractFacade's getEntityManager() method is overridden.
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /* 
    This constructor method invokes the parent abstract class AbstractFacade.java's
    constructor method AbstractFacade, which in turn initializes its entityClass instance
    variable with the UserPhoto class object reference returned by the UserPhoto.class.
     */
    public UserPhotoFacade() {
        super(UserPhoto.class);
    }

    /**
     * @param primaryKey is the Primary Key of the User entity in a table row in the database.
     * @return a list of photos associated with the User whose primary key is primaryKey
     */
    public List<UserPhoto> findPhotosByUserPrimaryKey(Integer primaryKey) {

        return (List<UserPhoto>) em.createNamedQuery("UserPhoto.findPhotosByUserDatabasePrimaryKey")
                .setParameter("primaryKey", primaryKey)
                .getResultList();
    }

    /* The following methods are inherited from the parent AbstractFacade class:
    
        create
        edit
        find
        findAll
        remove
     */
    
}
