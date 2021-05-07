/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
package edu.vt.managers;

import edu.vt.EntityBeans.User;
import edu.vt.FacadeBeans.AnswerFacade;
import edu.vt.FacadeBeans.QuestionFacade;
import edu.vt.FacadeBeans.QuizFacade;
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
    private QuizQuestion selectedQuestion;
    private AnswerChoice selectedAnswerChoice;
    private List<AnswerChoice> selectedAnswerChoices;
    private int questionPoint = 1;

    /*
    ==================
    Constructor Method
    ==================
     */
    public CreateQuizManager(){
        questions = new ArrayList<QuizQuestion>();
        selectedAnswerChoices = new ArrayList<>();
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
        this.questionTitle = questionTitle;
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

    public List<AnswerChoice> getSelectedAnswerChoices() {
        return selectedAnswerChoices;
    }

    public void setSelectedAnswerChoices(List<AnswerChoice> selectedAnswerChoices) {
        this.selectedAnswerChoices = selectedAnswerChoices;
    }

    public int getQuestionPoint() {
        return questionPoint;
    }

    public void setQuestionPoint(int questionPoint) {
        this.questionPoint = questionPoint;
    }

    //Methods

    public String submitQuiz() {
        System.out.println(quizTitle);
        System.out.println(quizTime);
        for (int i = 0; i < questions.size(); i++){
            System.out.println(questions.get(i).getQuestionText());
            for(int j = 0; j < questions.get(i).getAnswerChoices().size(); j++){
                System.out.println(questions.get(i).getAnswerChoices().get(j).getAnswerText());
            }
        }
        return null;
    }

    /*
    add answer choice
     */
    public void addAnswerChoice() {
        selectedAnswerChoices.add(new AnswerChoice("", false,"A",1,1, false));
        PrimeFaces.current().ajax().update("QuizAddForm:manage-question-content", "CreateQuizForm:dt-questions");
    }

    /*
    add answer choice by question
     */
    public void addAnswerChoiceByQuestion() {
        selectedQuestion.getAnswerChoices().add(new AnswerChoice("", false,"A",1,1,false));
        PrimeFaces.current().ajax().update("QuizEditForm:manage-question-content", "CreateQuizForm:dt-questions");
    }

    /*
    edit selected question
     */
    public void editSelectedQuestion(){
        clearQuestion();
        PrimeFaces.current().ajax().update("CreateQuizForm:messages", "CreateQuizForm:dt-questions");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("You have successfully edited your question"));
        PrimeFaces.current().executeScript("PF('editQuestionDialog').hide()");
        PrimeFaces.current().ajax().update("CreateQuizForm:messages");
    }

    /*
    delete selected question
     */
    public void deleteSelectedQuestion() {
        this.questions.remove(this.selectedQuestion);
        this.selectedQuestion = null;
        questionNumber--;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Question Removed"));
        PrimeFaces.current().ajax().update("CreateQuizForm:messages", "CreateQuizForm:dt-questions");
    }

    /*
    delete selected answer choice
     */
    public void deleteSelectedAnswerChoice(AnswerChoice choice) {
        this.selectedAnswerChoices.remove(choice);
        PrimeFaces.current().ajax().update("QuizAddForm:manage-question-content", "QuizAddForm:manage-question-content");
    }

    /*
    delete selected answer choice by question
     */
    public void deleteSelectedAnswerChoiceByQuestion(AnswerChoice choice) {
        this.selectedQuestion.getAnswerChoices().remove(choice);
        PrimeFaces.current().ajax().update("QuizEditForm:manage-question-content");
    }

    /*
    create question
     */
    public void createQuestion(){
        this.selectedQuestion = new QuizQuestion(1, questionTitle, questionPoint, questionNumber, this.selectedAnswerChoices);
        this.questions.add(this.selectedQuestion);
        questionNumber++;
        clearQuestion();
        PrimeFaces.current().ajax().update("CreateQuizForm:messages", "CreateQuizForm:dt-questions");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Question Added"));
        PrimeFaces.current().executeScript("PF('manageQuestionDialog').hide()");
        PrimeFaces.current().ajax().update("CreateQuizForm:messages");
    }

    /*
    reset question
     */
    public void clearQuestion(){
        questionTitle = null;
        questionPoint = 1;
        selectedAnswerChoices = new ArrayList<AnswerChoice>();
    }

    /*
    reset all question
     */
    public void clearAllQuestions() {
        this.questions = new ArrayList<QuizQuestion>();
        this.selectedAnswerChoices = new ArrayList<AnswerChoice>();
    }
}
