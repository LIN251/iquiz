/*
 * Created by Calvin Huang on 2021.3.24
 * Copyright © 2021 Calvin Huang. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.Answer;
import edu.vt.EntityBeans.Question;
import edu.vt.EntityBeans.Quiz;
import edu.vt.EntityBeans.Taker;
import edu.vt.FacadeBeans.AnswerFacade;
import edu.vt.FacadeBeans.QuestionFacade;
import edu.vt.FacadeBeans.QuizFacade;
import edu.vt.FacadeBeans.TakerFacade;
import edu.vt.globals.Methods;
import edu.vt.pojo.AnswerChoice;
import edu.vt.pojo.QuizQuestion;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @EJB
    private QuestionFacade questionFacade;

    @EJB
    private AnswerFacade answerFacade;



    private String searchQuery;
    private String takerName;
    private QuizQuestion selectedQuestion ;
    private AnswerChoice selectedAnswerChoice ;
    List<Question> questionListForOneQuiz = new ArrayList<Question>();
    private List<QuizQuestion> questions= new ArrayList<QuizQuestion>();
    private List<AnswerChoice> answerChoices = new ArrayList<AnswerChoice>();
    List<Answer> answerListForOneQuestion = new ArrayList<Answer>();
    private List<AnswerChoice> selectedAnswerChoices = new ArrayList<AnswerChoice>();
    Quiz aQuiz = new Quiz();
    private Integer totalPoints;

    //------------------------------------------setter and getter ------------------------------------------

    public TakerFacade getTakerFacade() {
        return takerFacade;
    }

    public void setTakerFacade(TakerFacade takerFacade) {
        this.takerFacade = takerFacade;
    }

    public QuizFacade getQuizFacade() {
        return quizFacade;
    }

    public Quiz getaQuiz() {
        return aQuiz;
    }

    public AnswerFacade getAnswerFacade() {
        return answerFacade;
    }

    public void setAnswerFacade(AnswerFacade answerFacade) {
        this.answerFacade = answerFacade;
    }

    public List<Answer> getAnswerListForOneQuestion() {
        return answerListForOneQuestion;
    }

    public void setAnswerListForOneQuestion(List<Answer> answerListForOneQuestion) {
        this.answerListForOneQuestion = answerListForOneQuestion;
    }

    public void setaQuiz(Quiz aQuiz) {
        this.aQuiz = aQuiz;
    }

    public void setQuizFacade(QuizFacade quizFacade) {
        this.quizFacade = quizFacade;
    }

    public List<Question> getQuestionListForOneQuiz() {
        return questionListForOneQuiz;
    }

    public void setQuestionListForOneQuiz(List<Question> questionListForOneQuiz) {
        this.questionListForOneQuiz = questionListForOneQuiz;
    }

    public QuestionFacade getQuestionFacade() {
        return questionFacade;
    }

    public void setQuestionFacade(QuestionFacade questionFacade) {
        this.questionFacade = questionFacade;
    }

    public String getTakerName() {
        return takerName;
    }

    public void setTakerName(String takerName) {
        this.takerName = takerName;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public QuizQuestion getSelectedQuestion() {
        return selectedQuestion;
    }

    public void setSelectedQuestion(QuizQuestion selectedQuestion) {
        this.selectedQuestion = selectedQuestion;
    }

    public AnswerChoice getSelectedAnswerChoice() {
        return selectedAnswerChoice;
    }

    public void setSelectedAnswerChoice(AnswerChoice selectedAnswerChoice) {
        this.selectedAnswerChoice = selectedAnswerChoice;
    }

    public List<QuizQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuizQuestion> questions) {
        this.questions = questions;
    }

    public List<AnswerChoice> getAnswerChoices() {
        return answerChoices;
    }

    public void setAnswerChoices(List<AnswerChoice> answerChoices) {
        this.answerChoices = answerChoices;
    }

    public List<AnswerChoice> getSelectedAnswerChoices() {
        return selectedAnswerChoices;
    }

    public void setSelectedAnswerChoices(List<AnswerChoice> selectedAnswerChoices) {
        this.selectedAnswerChoices = selectedAnswerChoices;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }
    //------------------------------------------------- END -------------------------------------------------

    public AccessCodeController() {
        totalPoints = 0;
    }


    public void performSearch() {
        reset();
        questionListForOneQuiz = new ArrayList<Question>();
        aQuiz = getQuizFacade().findOneQuiz(searchQuery);
//        aQuiz.getTimeLimit()
        if (aQuiz == null) {
            // Quiz Does Not Exists
            Methods.showMessage("Fatal Error", "Quiz Does Not Exists!", "Please Try a Different One!");

        }
        else{
            //update taker
            Taker newTaker = new Taker();
            newTaker.setFirstName(takerName);
            newTaker.setLastName("taker");
            getTakerFacade().create(newTaker);
            //get all questions
            questionListForOneQuiz = getQuestionFacade().findAllquestions(aQuiz.getId());
            //collect all questions and answers
            for (int i = 0; i < questionListForOneQuiz.size(); i++) {
                answerListForOneQuestion = getAnswerFacade().findAllAnswersForOneQuestion(questionListForOneQuiz.get(i).getId());
                answerChoices = new ArrayList<AnswerChoice>();
                for (int x = 0; x < answerListForOneQuestion.size(); x++) {
                    answerChoices.add(new AnswerChoice(answerListForOneQuestion.get(x).getAnswer_text(), false, getCharForNumber(x+1)));
                }
                QuizQuestion initialQuestion = new QuizQuestion(questionListForOneQuiz.get(i).getQuestionText(), questionListForOneQuiz.get(i).getQuestionPoint(),i, answerChoices);
                questions.add(initialQuestion);
                totalPoints += initialQuestion.getQuestionPoint();
            }
            //show message
            Methods.showMessage("Information", "Success!", "Openning the quiz!");
        }
    }

    public String performSearchByAccessCode(String access_code) {
        reset();
        questionListForOneQuiz = new ArrayList<Question>();
        aQuiz = getQuizFacade().findOneQuiz(access_code);
//        aQuiz.getTimeLimit()
        if (aQuiz == null) {
            // Quiz Does Not Exists
            Methods.showMessage("Fatal Error", "Quiz Does Not Exists!", "Please Try a Different One!");
            return null;
        }
        else{
            //update taker
            Taker newTaker = new Taker();
            newTaker.setFirstName(takerName);
            newTaker.setLastName("taker");
            getTakerFacade().create(newTaker);
            //get all questions
            questionListForOneQuiz = getQuestionFacade().findAllquestions(aQuiz.getId());
            //collect all questions and answers
            for (int i = 0; i < questionListForOneQuiz.size(); i++) {
                answerListForOneQuestion = getAnswerFacade().findAllAnswersForOneQuestion(questionListForOneQuiz.get(i).getId());
                answerChoices = new ArrayList<AnswerChoice>();
                for (int x = 0; x < answerListForOneQuestion.size(); x++) {
                    answerChoices.add(new AnswerChoice(answerListForOneQuestion.get(x).getAnswer_text(), false, getCharForNumber(x+1)));
                }
                QuizQuestion initialQuestion = new QuizQuestion(questionListForOneQuiz.get(i).getQuestionText(), questionListForOneQuiz.get(i).getQuestionPoint(),i, answerChoices);
                questions.add(initialQuestion);
                totalPoints += initialQuestion.getQuestionPoint();
            }
            //show message
            Methods.showMessage("Information", "Success!", "Openning the quiz!");
            return "/quizzes/TakeQuiz?faces-redirect=true";
        }
    }

    private void reset() {
        questionListForOneQuiz = new ArrayList<Question>();
        questions= new ArrayList<QuizQuestion>();
        answerChoices = new ArrayList<AnswerChoice>();
        answerListForOneQuestion = new ArrayList<Answer>();
        selectedAnswerChoices = new ArrayList<AnswerChoice>();
        aQuiz = new Quiz();
        totalPoints = 0;
    }


    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
    }


}
