/*
 * Created by Calvin Huang on 2021.3.24
 * Copyright © 2021 Calvin Huang. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.Quiz;
import edu.vt.EntityBeans.Taker;
import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserPhoto;
import edu.vt.FacadeBeans.QuizFacade;
import edu.vt.FacadeBeans.TakerFacade;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.FacadeBeans.UserPhotoFacade;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;
import edu.vt.globals.Password;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
---------------------------------------------------------------------------
The @Named (javax.inject.Named) annotation indicates that the objects
instantiated from this class will be managed by the Contexts and Dependency
Injection (CDI) container. The name "userController" is used within
Expression Language (EL) expressions in JSF (XHTML) facelets pages to
access the properties and invoke methods of this class.
---------------------------------------------------------------------------
 */
@Named("accessCodeController")

/*
The @SessionScoped annotation preserves the values of the UserController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped

/*
--------------------------------------------------------------------------
Marking the UserController class as "implements Serializable" implies that
instances of the class can be automatically serialized and deserialized. 

Serialization is the process of converting a class instance (object)
from memory into a suitable format for storage in a file or memory buffer, 
or for transmission across a network connection link.

Deserialization is the process of recreating a class instance (object)
in memory from the format under which it was stored.
--------------------------------------------------------------------------
 */
public class AccessCodeController implements Serializable {

    @EJB
    private QuizFacade quizFacade;


    @EJB
    private TakerFacade takerFacade;


    public TakerFacade getTakerFacade() {
        return takerFacade;
    }

    public void setTakerFacade(TakerFacade takerFacade) {
        this.takerFacade = takerFacade;
    }

    public QuizFacade getQuizFacade() {
        return quizFacade;
    }

    public void setQuizFacade(QuizFacade quizFacade) {
        this.quizFacade = quizFacade;
    }

    private String searchQuery;

    public String getTakerName() {
        return takerName;
    }
    public void printHello() {
     System.out.println("Hello");
    }

    public void setTakerName(String takerName) {
        this.takerName = takerName;
    }

    private String takerName;
    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public AccessCodeController() {
    }


    public String performSearch() {
        Quiz aQuiz = getQuizFacade().findOneQuiz(searchQuery);
        if (aQuiz == null) {
            // A user already exists with the username entered by the user
            Methods.showMessage("Fatal Error", "Quiz Does Not Exists!", "Please Try a Different One!");
            return "";
        }
        else{
            Taker newTaker = new Taker();
            newTaker.setFirstName(takerName);
            newTaker.setLastName("taker");
            getTakerFacade().create(newTaker);

            Methods.showMessage("Information", "Success!", "Openning the quiz!");

            return "/quizzes/takeQuiz?faces-redirect=true";
        }
    }

}
