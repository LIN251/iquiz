
/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
package edu.vt.controllers;


import edu.vt.EntityBeans.*;
import edu.vt.FacadeBeans.*;
import edu.vt.globals.Methods;
import edu.vt.pojo.AnswerChoice;
import edu.vt.pojo.QuizQuestion;
import org.primefaces.PrimeFaces;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/*
---------------------------------------------------------------------------
The @Named (javax.inject.Named) annotation indicates that the objects
instantiated from this class will be managed by the Contexts and Dependency
Injection (CDI) container. The name "myQuizController" is used within
Expression Language (EL) expressions in JSF (XHTML) facelets pages to
access the properties and invoke methods of this class.
---------------------------------------------------------------------------
 */
@Named("myQuizController")

/*
The @SessionScoped annotation preserves the values of the MyQuizController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped

/*
--------------------------------------------------------------------------
Marking the MyQuizController class as "implements Serializable" implies that
instances of the class can be automatically serialized and deserialized.

Serialization is the process of converting a class instance (object)
from memory into a suitable format for storage in a file or memory buffer,
or for transmission across a network connection link.

Deserialization is the process of recreating a class instance (object)
in memory from the format under which it was stored.
--------------------------------------------------------------------------
 */
public class MyQuizController implements Serializable {

    /*
    ===============================
    Instance Variables (Properties)
    ===============================

    The @EJB annotation directs the storage (injection) of the object
    reference of the JPA QuizFacade class object into the instance
    variable QuizFacade below after it is instantiated at runtime.
    */
    @EJB
    private QuizFacade quizFacade;
    /*
    The @EJB annotation directs the storage (injection) of the object
    reference of the JPA QuestionFacade class object into the instance
    variable QuestionFacade below after it is instantiated at runtime.
    */
    @EJB
    private QuestionFacade questionFacade;
    /*
    The @EJB annotation directs the storage (injection) of the object
    reference of the JPA AnswerFacade class object into the instance
    variable AnswerFacade below after it is instantiated at runtime.
    */
    @EJB
    private AnswerFacade answerFacade;
    /*
    The @EJB annotation directs the storage (injection) of the object
    reference of the JPA AttemptFacade class object into the instance
    variable AttemptFacade below after it is instantiated at runtime.
    */
    @EJB
    private AttemptFacade attemptFacade;
    /*
    The @EJB annotation directs the storage (injection) of the object
    reference of the JPA AttemptAnswerFacade class object into the instance
    variable AttemptAnswerFacade below after it is instantiated at runtime.
    */
    @EJB
    private AttemptAnswerFacade attemptAnswerFacade;

    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private List<Quiz> items;
    private List<Question> questionItems = null;
    private List<Answer> answerItems = null;
    private ArrayList<QuizQuestion> quizQuestions;
    private Integer totalPoints;
    private Quiz selected;
    private Quiz selectedQuiz = null;
    private int selectedID;
    private String selectedAccessCode;
    private List<Integer> did;

    /*
    ==================
    Constructor Method
    ==================
    */
    public MyQuizController() {
        quizQuestions = new ArrayList<QuizQuestion>();
        totalPoints = 0;
    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public QuizFacade getQuizFacade() { return quizFacade; }

    public void setQuizFacade(QuizFacade quizFacade) { this.quizFacade = quizFacade; }

    public QuestionFacade getQuestionFacade() { return questionFacade; }

    public void setQuestionFacade(QuestionFacade questionFacade) { this.questionFacade = questionFacade; }

    public AnswerFacade getAnswerFacade() { return answerFacade; }

    public void setAnswerFacade( AnswerFacade answerFacade) { this.answerFacade = answerFacade; }

    public Quiz getSelected() { return selected; }

    public void setSelected(Quiz selected) { this.selected = selected; }

    public AttemptFacade getAttemptFacade() { return attemptFacade; }

    public void setAttemptFacade(AttemptFacade attemptFacade) { this.attemptFacade = attemptFacade; }

    public AttemptAnswerFacade getAttemptAnswerFacade() { return attemptAnswerFacade; }

    public void setAttemptAnswerFacade(AttemptAnswerFacade attemptAnswerFacade) { this.attemptAnswerFacade = attemptAnswerFacade; }

    public List<Quiz> getItems() {
        User signedInUser = (User) Methods.sessionMap().get("user");
        items = null;
        if (items == null) {
            items = getQuizFacade().findQuizByUserId(signedInUser.getId());
        }
        return items;
    }

    /*
    Unused function
     */
    public String getRandomString() {
        Random rand = new Random();
        int upperbound = 300;
        int int_random = rand.nextInt(upperbound);
        return "haha" + int_random;
    }

    /*
    Unused function
     */
    public List<Question> getQuestionItems() {
        return questionItems;
    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public Quiz getSelectedQuiz() { return selectedQuiz; }

    public List<QuizQuestion> getQuizQuestions() { return quizQuestions; }

    public Integer getTotalPoints() { return totalPoints; }

    public void setItems(List<Quiz> items) { this.items = items; }

    public int getSelectedID() { return selectedID; }

    public void setSelectedID(int selectedID) { this.selectedID = selectedID; }

    public String getSelectedAccessCode() { return selectedAccessCode; }

    public void setSelectedAccessCode(String selectedAccessCode) { this.selectedAccessCode = selectedAccessCode; }

    /**
     * Delete the quiz
     * @param quizID the quiz id
     * @return go to address of MyQuizzes page
     */
    public String deleteQuiz(int quizID) {
        System.out.println(quizID);
        List<Question> questions = getQuestionFacade().findQuestionByQuizId(quizID);
        for(int i = 0; i < questions.size(); i++) {
            List<Answer> answers = getAnswerFacade().findAllAnswersForOneQuestion(questions.get(i).getId());
            for(int j = 0; j < answers.size(); j++) {
                List<AttemptAnswer> attemptAnswerList = getAttemptAnswerFacade().findAttemptAnswerByAnswerId(answers.get(j).getId());
                for(int k = 0; k < attemptAnswerList.size(); k++) {
                    getAttemptAnswerFacade().remove(attemptAnswerList.get(k));
                }
                getAnswerFacade().remove(answers.get(j));
            }
            getQuestionFacade().remove(questions.get(i));
        }
        List<Attempt> attempts = getAttemptFacade().findAllAttemptByQuizId(quizID);
        for(int j = 0; j < attempts.size(); j++) {
            getAttemptFacade().remove(attempts.get(j));
        }
        Quiz quiz = getQuizFacade().findQuizByID(quizID);
        System.out.println(quiz.getTitle());
        getQuizFacade().remove(quiz);
        System.out.println("remove quiz");
        addMessage("Confirmed", "This quiz has been deleted");
        return "/quizzes/MyQuizzes?faces-redirect=true";
    }

    /**
     * Delete the quiz without quiz id
     * @return go to address of MyQuizzes page
     */
    public String deleteQuiz() {
        System.out.println(selectedID);
        return "/quizzes/MyQuizzes?faces-redirect=true";
    }

    /**
     * Load the questions and quizzes from database
     * @param id the quiz id
     * @return go to address of MyQuizzes page
     */
    public String updateQuestion(int id) {
        questionItems = null;
        selectedQuiz = null;
        answerItems = null;
        quizQuestions.clear();
        totalPoints = 0;

        if(questionItems == null) {
            questionItems = getQuestionFacade().findQuestionByQuizId(id);
        }
        if(selectedQuiz == null) {
            selectedQuiz = getQuizFacade().findQuizByID(id);
        }
        if(answerItems == null) {
            for(int i = 0; i < questionItems.size(); i++) {
                answerItems = getAnswerFacade().findAllAnswersForOneQuestion(questionItems.get(i).getId());
                ArrayList<AnswerChoice> everyAnswer = new ArrayList<AnswerChoice>();
                for(int j = 0; j < answerItems.size(); j++) {
                    everyAnswer.add(new AnswerChoice(answerItems.get(j).getAnswer_text(), answerItems.get(j).isInstructorResult(), getCharForNumber(j + 1),i,answerItems.get(j).getId(), false));
                }
                quizQuestions.add(new QuizQuestion(questionItems.get(i).getId(),questionItems.get(i).getQuestionText(), questionItems.get(i).getQuestionPoint(), questionItems.get(i).getId(), everyAnswer));

            }
        }
        if(totalPoints == 0) {
            for(int i = 0; i < questionItems.size(); i++) {
                totalPoints += questionItems.get(i).getQuestionPoint();
            }
        }
        return "/quizzes/MyQuizzesView?faces-redirect=true";
    }

    /**
     * CLone the quiz
     * @param quizID the quiz id
     */
    public void cloneQuiz(int quizID) {
        Quiz quiz = getQuizFacade().findQuizByID(quizID);
        String accessCode = randomAccessCode();
        quiz.setAccessCode(accessCode);
        quiz.setPublish(false);
        quiz.setId(null);
        quiz.setTitle(quiz.getTitle() + "-copy");
        quiz.setPublishAt(new Date());
        quizFacade.create(quiz);
        List<Question> questions = questionFacade.findAllquestions(quizID);
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            List<Answer> answers = answerFacade.findAllAnswersForOneQuestion(q.getId());
            q.setId(null);
            q.setQuizID(quiz.getId());
            questionFacade.create(q);
            for (int j = 0; j < answers.size(); j++) {
                Answer a = answers.get(j);
                a.setId(null);
                a.setQuestionId(q.getId());
                answerFacade.create(a);
            }
        }
        String mess = "You have cloned your quiz!";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mess));
        PrimeFaces.current().ajax().update("MyQuizzesListForm:msg", "MyQuizzesListForm:dt-quizzes");
    }

    /**
     * Publish or Unpublish the quiz
     * @param quizID the quiz id
     * @param judge judge if the quiz is published
     * @return true
     */
    public boolean updateQuiz(int quizID, boolean judge) {
        Quiz quiz = getQuizFacade().findQuizByID(quizID);
        quiz.setPublish(!judge);
        quizFacade.edit(quiz);
        String pub;
        if (quiz.isPublish()){
            pub = "published";
        }else{
            pub = "unpublished";
        }
        String mess = "Your quiz is now " + pub + "!";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mess));
        PrimeFaces.current().ajax().update("MyQuizzesListForm:msg");
        return true;
    }

    /**
     * change the date format
     * @param date the date
     * @return the new format
     */
    public String changeDateFormat(Date date) {
        String format = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(format);
        return sdf.format(date);
    }

    /**
     * Generate the link string for a particular quiz
     * @param accessCode the access code of the quiz
     * @return the link as string
     */
    public String selectedQuizLink(String accessCode) {
        //Get the server name
        String serverName = FacesContext.getCurrentInstance().getExternalContext().getRequestServerName();
        //Get the port number
        int port  = FacesContext.getCurrentInstance().getExternalContext().getRequestServerPort();
        //Combine to the get final ink
        String link = "http://" + serverName + ":" + String.valueOf(port) + "/iquiz/quizzes/AccessQuiz.xhtml?access_code="
                + accessCode;
        return link;
    }

    /**
     * Disable the button if the taker attempt the quiz
     * @param quizId the quiz id
     * @return true or false
     */
    public Boolean disableButton(int quizId) {
        List<Attempt> attempts = getAttemptFacade().findAllAttemptByQuizId(quizId);
        if(attempts.size() != 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Show messages on the screen
     * @param summary the summary want to show
     * @param detail the detail want to show
     */
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /**
     * Convert the number to a b c d format
     * @param i the number waiting for convert
     * @return the a b c d format
     */
    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
    }

    /**
     * Generate the random access code
     * @return the random access code
     */
    public String randomAccessCode() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 12;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}
