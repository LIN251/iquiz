/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.Answer;
import edu.vt.EntityBeans.Question;
import edu.vt.EntityBeans.Quiz;
import edu.vt.EntityBeans.Taker;
import edu.vt.FacadeBeans.*;
import edu.vt.globals.Methods;
import edu.vt.pojo.AnswerChoice;
import edu.vt.pojo.QuizQuestion;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;import edu.vt.EntityBeans.*;

import org.primefaces.event.SelectEvent;

/*
---------------------------------------------------------------------------
The @Named (javax.inject.Named) annotation indicates that the objects
instantiated from this class will be managed by the Contexts and Dependency
Injection (CDI) container. The name "accessCodeController" is used within
Expression Language (EL) expressions in JSF (XHTML) facelets pages to
access the properties and invoke methods of this class.
---------------------------------------------------------------------------
 */
@Named("accessCodeController")

/*
The @SessionScoped annotation preserves the values of the AccessCodeController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped

/*
--------------------------------------------------------------------------
Marking the AccessCodeController class as "implements Serializable" implies that
instances of the class can be automatically serialized and deserialized. 

Serialization is the process of converting a class instance (object)
from memory into a suitable format for storage in a file or memory buffer, 
or for transmission across a network connection link.

Deserialization is the process of recreating a class instance (object)
in memory from the format under which it was stored.
--------------------------------------------------------------------------
 */
public class AccessCodeController implements Serializable {

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
    reference of the JPA TakerFacade class object into the instance
    variable TakerFacade below after it is instantiated at runtime.
    */
    @EJB
    private TakerFacade takerFacade;
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
    private Taker newTaker;
    private int studentScore;
    private AnswerChoice selectedAns;
    ArrayList<Integer> answerList ;
    ArrayList<Integer> questionList ;
    int timeLimit;

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
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

    public void setAnswerListForOneQuestion(List<Answer> answerListForOneQuestion) { this.answerListForOneQuestion = answerListForOneQuestion; }

    public void setaQuiz(Quiz aQuiz) {
        this.aQuiz = aQuiz;
    }

    public void setQuizFacade(QuizFacade quizFacade) {
        this.quizFacade = quizFacade;
    }

    public List<Question> getQuestionListForOneQuiz() {
        return questionListForOneQuiz;
    }

    public void setQuestionListForOneQuiz(List<Question> questionListForOneQuiz) { this.questionListForOneQuiz = questionListForOneQuiz; }

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

    public void setSelectedAnswerChoice(AnswerChoice selectedAnswerChoice) { this.selectedAnswerChoice = selectedAnswerChoice; }

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

    public void setSelectedAnswerChoices(List<AnswerChoice> selectedAnswerChoices) { this.selectedAnswerChoices = selectedAnswerChoices; }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getStudentScore() {
        return studentScore;
    }

    public void setStudentScore(int studentScore) {
        this.studentScore = studentScore;
    }

    public AnswerChoice getSelectedAns() {
        return selectedAns;
    }

    public void setSelectedAns(AnswerChoice selectedAns) {
        this.selectedAns = selectedAns;
    }

    public Taker getNewTaker() {
        return newTaker;
    }

    public void setNewTaker(Taker newTaker) {
        this.newTaker = newTaker;
    }

    public AttemptFacade getAttemptFacade() {
        return attemptFacade;
    }

    public void setAttemptFacade(AttemptFacade attemptFacade) {
        this.attemptFacade = attemptFacade;
    }

    public AttemptAnswerFacade getAttemptAnswerFacade() {
        return attemptAnswerFacade;
    }

    public void setAttemptAnswerFacade(AttemptAnswerFacade attemptAnswerFacade) { this.attemptAnswerFacade = attemptAnswerFacade; }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    /*
    ==================
    Constructor Method
    ==================
     */
    public AccessCodeController() {
        totalPoints = 0;
    }


    /**
     * Show the dialog when taking the quiz
     * @return the take quiz address
     */
    public String performSearch() {
        reset();
        questionListForOneQuiz = new ArrayList<Question>();
        aQuiz = getQuizFacade().findOneQuiz(searchQuery);

        if (aQuiz == null ){
            Methods.showMessage("Fatal Error", "Quiz Does Not Exists!", "Please Try a Different One!");

            return null;
        }

        if (!aQuiz.isPublish()){
            Methods.showMessage("Fatal Error", "Quiz does not published!", "");

            return null;
        }

        timeLimit = aQuiz.getTimeLimit();
        if (aQuiz == null) {
            // Quiz Does Not Exists
            Methods.showMessage("Fatal Error", "Quiz Does Not Exists!", "Please Try a Different One!");

        }
        else{
            //update taker
            newTaker = new Taker();
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
                    answerChoices.add(new AnswerChoice(answerListForOneQuestion.get(x).getAnswer_text(), answerListForOneQuestion.get(x).isInstructorResult(), getCharForNumber(x+1),i,answerListForOneQuestion.get(x).getId(), false));
                }
                QuizQuestion initialQuestion = new QuizQuestion(questionListForOneQuiz.get(i).getId(),questionListForOneQuiz.get(i).getQuestionText(), questionListForOneQuiz.get(i).getQuestionPoint(),i, answerChoices);
                questions.add(initialQuestion);
                totalPoints += initialQuestion.getQuestionPoint();
            }
            //show message
            Methods.showMessage("Information", "Success!", "Openning the quiz!");
            return "/quizzes/TakeQuiz?faces-redirect=true";

        }
        return null;
    }

    /**
     * Go to take quiz address by access code
     * @param access_code the access code
     * @return the take quiz address
     */
    public String performSearchByAccessCode(String access_code) {
        reset();
        questionListForOneQuiz = new ArrayList<Question>();
        aQuiz = getQuizFacade().findOneQuiz(access_code);



        if (aQuiz == null ){
            Methods.showMessage("Fatal Error", "Quiz Does Not Exists!", "Please Try a Different One!");

            return null;
        }

        if (!aQuiz.isPublish()){
            Methods.showMessage("Fatal Error", "Quiz does not published!", "");

            return null;
        }

        timeLimit = aQuiz.getTimeLimit();
        if (aQuiz == null) {
            // Quiz Does Not Exists
            Methods.showMessage("Fatal Error", "Quiz Does Not Exists!", "Please Try a Different One!");
            return null;
        }
        else{
            //update taker
            newTaker = new Taker();
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
                    answerChoices.add(new AnswerChoice(answerListForOneQuestion.get(x).getAnswer_text(), answerListForOneQuestion.get(x).isInstructorResult(), getCharForNumber(x+1),i,answerListForOneQuestion.get(x).getId(),false));
                }
                QuizQuestion initialQuestion = new QuizQuestion(questionListForOneQuiz.get(i).getId(),questionListForOneQuiz.get(i).getQuestionText(), questionListForOneQuiz.get(i).getQuestionPoint(),i, answerChoices);
                questions.add(initialQuestion);
                totalPoints += initialQuestion.getQuestionPoint();
            }
            //show message
            Methods.showMessage("Information", "Success!", "Openning the quiz!");
            return "/quizzes/TakeQuiz?faces-redirect=true";
        }
    }

    /**
     * Reset all variables
     */
    private void reset() {
        selectedAns = null;
        timeLimit=0;
        questionListForOneQuiz = new ArrayList<Question>();
        questions= new ArrayList<QuizQuestion>();
        answerChoices = new ArrayList<AnswerChoice>();
        answerListForOneQuestion = new ArrayList<Answer>();
        selectedAnswerChoices = new ArrayList<AnswerChoice>();
        aQuiz = new Quiz();
        totalPoints = 0;
        studentScore = 0;
        answerList = new ArrayList<Integer>();
        questionList = new ArrayList<Integer>();
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
     * Select the row when taking the quiz
     * @param event the answer choice selected
     */
    public void onRowSelect(SelectEvent<AnswerChoice> event) {
        if (answerList.contains(selectedAns.getBelongsTo())){
            questionList.set(answerList.indexOf(selectedAns.getBelongsTo()), selectedAns.getAnswerChoiceID());
        }
        else{
            answerList.add(selectedAns.getBelongsTo());
            questionList.add(selectedAns.getAnswerChoiceID());
        }
    }



    public String process_submission() {

        Attempt newAttempt = new Attempt();
        newAttempt.setTakerId(getNewTaker().getId());
        newAttempt.setQuizID(aQuiz.getId());
        newAttempt.setScore(0);
        getAttemptFacade().create(newAttempt);

        studentScore = 0;
        for(int i = 0; i<questionList.size();i++){
            int questionID =getAnswerFacade().findQuestionIDByAnswerID(questionList.get(i));
            AttemptAnswer newAttemptAnswer = new AttemptAnswer();
            newAttemptAnswer.setAttemptID(newAttempt.getId());
            newAttemptAnswer.setAnswerID(questionList.get(i));
            newAttemptAnswer.setQuestionID(questionID);
            getAttemptAnswerFacade().create(newAttemptAnswer);

            Answer aAnswer= getAnswerFacade().findAnswerByAnswerID(questionList.get(i));
            if(aAnswer.isInstructorResult()){
                Question aquestion = getQuestionFacade().findQuestionByQuestionId(questionID);
                studentScore = studentScore + aquestion.getQuestionPoint();
            }

        }
        newAttempt.setScore(studentScore);
        getAttemptFacade().edit(newAttempt);
        System.out.println(studentScore);

        Attempt a = attemptFacade.find(newAttempt.getId());
        System.out.println( a.getScore());
        return "/quizzes/AttemptResult?faces-redirect=true";
    }
}
