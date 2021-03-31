/*
 * Created by Calvin Huang on 2021.3.24
 * Copyright © 2021 Calvin Huang. All rights reserved.
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserFacade extends AbstractFacade<User> {
    /*
    ------------------------------------------------------------------------------------------------------
    Annotating 'private EntityManager em;' with '@PersistenceContext(unitName = "CloudDrive-BalciPU")' 
    implies that the EntityManager instance pointed to by 'em' is associated with the 'CloudDrive-BalciPU'
    persistence context. The persistence context is a set of entity (User) instances in which for
    any persistent entity (User) identity, there is a unique entity (User) instance.
    Within the persistence context, the entity (User) instances and their life cycle are managed.
    The EntityManager API is used to create and remove persistent entity (User) instances,
    to find entities by their primary key, and to query over entities (Users).
    ------------------------------------------------------------------------------------------------------
     */
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
    variable with the User class object reference returned by the User.class.
     */
    public UserFacade() {
        super(User.class);
    }

    /**
     * @param id is the Primary Key of the User entity in a table row in the database.
     * @return object reference of the User object whose primary key is id
     */
    public User getUser(int id) {
        
        // The find method is inherited from the parent AbstractFacade class
        return em.find(User.class, id);
    }

    /**
     * @param username is the username attribute (column) value of the user
     * @return object reference of the User entity whose user name is username
     */
    public User findByUsername(String username) {
        if (em.createQuery("SELECT c FROM User c WHERE c.username = :username")
                .setParameter("username", username)
                .getResultList().isEmpty()) {
            return null;
        } else {
            return (User) (em.createQuery("SELECT c FROM User c WHERE c.username = :username")
                    .setParameter("username", username)
                    .getSingleResult());
        }
    }

    /**
     * Deletes the User entity whose primary key is id
     * @param id is the Primary Key of the User entity in a table row in the database.
     */
    public void deleteUser(int id) {
        
        // The find method is inherited from the parent AbstractFacade class
        User user = em.find(User.class, id);
        
        // The remove method is inherited from the parent AbstractFacade class
        em.remove(user); 
    }

}
