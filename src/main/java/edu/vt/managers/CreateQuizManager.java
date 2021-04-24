/*
 * Created by Calvin Huang on 2021.3.24
 * Copyright © 2021 Calvin Huang. All rights reserved.
 */
package edu.vt.managers;

import edu.vt.EntityBeans.User;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.globals.Methods;
import edu.vt.globals.Password;
import edu.vt.pojo.AnswerChoice;
import edu.vt.pojo.QuizQuestion;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named(value = "createQuizManager")
@SessionScoped

public class CreateQuizManager implements Serializable {

    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private String quizTitle;
    private String quizTime;
    private int questionNumber = 1;
    private List<QuizQuestion> questions;
    private String questionTitle;
    private List<AnswerChoice> answerChoices;
    private List<QuizQuestion> selectedQuestions;
    private QuizQuestion selectedQuestion;
    private AnswerChoice selectedAnswerChoice;
    private List<AnswerChoice> selectedAnswerChoices;
    /*
    The instance variable 'userFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the WildFly AS) to inject (store) the object reference
    of the UserFacade object, after it is instantiated at runtime, into the instance variable 'userFacade'.
     */

    /*
    ==================
    Constructor Method
    ==================
     */
//    @PostConstruct
//    public void init() {
//        questions = new ArrayList<Question>();
//        Question initialQuestion = new Question("hello", 1, 1);
//        questions.add(initialQuestion);
//    }
    public CreateQuizManager(){
        questions = new ArrayList<QuizQuestion>();
        answerChoices = new ArrayList<AnswerChoice>();
        selectedAnswerChoices = new ArrayList<>();
        answerChoices.add(new AnswerChoice("Washington DC", false));
        answerChoices.add(new AnswerChoice("Richmond", false));
        answerChoices.add(new AnswerChoice("Blacksburg", true));
        answerChoices.add(new AnswerChoice("New York", false));
        QuizQuestion initialQuestion = new QuizQuestion("Example", 1, questionNumber, answerChoices);
        questions.add(initialQuestion);
        questions.add(initialQuestion);
    }
    /*
    =========================
    Getter and Setter Methods
    =========================
     */

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public String getQuizTime() {
        return quizTime;
    }

    public void setQuizTime(String quizTime) {
        this.quizTime = quizTime;
    }

    public List<QuizQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuizQuestion> questions) {
        this.questions = questions;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        System.out.println("qqqqqq");
        this.questionTitle = questionTitle;
    }

    public List<QuizQuestion> getSelectedQuestions() {
        return selectedQuestions;
    }

    public void setSelectedQuestions(List<QuizQuestion> selectedQuestions) {
        this.selectedQuestions = selectedQuestions;
    }

    public QuizQuestion getSelectedQuestion() {
        return selectedQuestion;
    }

    public void setSelectedQuestion(QuizQuestion selectedQuestion) {
        this.selectedQuestion = selectedQuestion;
    }

    /*
                            ================
                            Instance Methods
                            ================

                            *****************************************************
                            Sign in the User if the Entered Username and Password
                            are Valid and Redirect to Show the Profile Page
                            *****************************************************
                             */
    public String submitQuiz() {
        return null;
    }

    public void addQuestion() {
        questionNumber++;
        questions.add(new QuizQuestion("", 1, questionNumber, answerChoices));
    }

    public void addAnswerChoice() {
        selectedAnswerChoices.add(new AnswerChoice("", false));
        PrimeFaces.current().ajax().update("QuizAddForm:manage-question-content", "CreateQuizForm:dt-questions");
    }

    public void deleteSelectedQuestion() {
        this.questions.remove(this.selectedQuestion);
        this.selectedQuestion = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Question Removed"));
        PrimeFaces.current().ajax().update("CreateQuizForm:messages", "CreateQuizForm:dt-questions");
    }

    public List<AnswerChoice> getAnswerChoices() {
        return answerChoices;
    }

    public void setAnswerChoices(List<AnswerChoice> answerChoices) {
        this.answerChoices = answerChoices;
    }

    public AnswerChoice getSelectedAnswerChoice() {
        return selectedAnswerChoice;
    }

    public void setSelectedAnswerChoice(AnswerChoice selectedAnswerChoice) {
        this.selectedAnswerChoice = selectedAnswerChoice;
    }

    public List<AnswerChoice> getSelectedAnswerChoices() {
        return selectedAnswerChoices;
    }

    public void setSelectedAnswerChoices(List<AnswerChoice> selectedAnswerChoices) {
        this.selectedAnswerChoices = selectedAnswerChoices;
    }
}
