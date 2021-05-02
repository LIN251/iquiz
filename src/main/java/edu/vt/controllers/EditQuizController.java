package edu.vt.controllers;
import edu.vt.EntityBeans.*;
import edu.vt.FacadeBeans.AnswerFacade;
import edu.vt.FacadeBeans.QuestionFacade;
import edu.vt.FacadeBeans.QuizFacade;
import edu.vt.FacadeBeans.TakerFacade;
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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Named("editQuizController")

/*
The @SessionScoped annotation preserves the values of the UserController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped
public class EditQuizController implements Serializable{
    private String quizTitle;
    private String quizTime;
    private List<QuizQuestion> questions;
    private int questionNumber = 1;

    private String questionTitle;
    private QuizQuestion selectedQuestion;
    private AnswerChoice selectedAnswerChoice;
    private List<AnswerChoice> selectedAnswerChoices;
    private int questionPoint = 1;
    private String accessCode;

    @EJB
    private QuizFacade quizFacade;

    @EJB
    private QuestionFacade questionFacade;

    @EJB
    private AnswerFacade answerFacade;

    public EditQuizController() {
        questions = new ArrayList<>();
        selectedAnswerChoices = new ArrayList<>();
    }

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

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public List<QuizQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuizQuestion> questions) {
        this.questions = questions;
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

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public QuizFacade getQuizFacade() {
        return quizFacade;
    }

    public void setQuizFacade(QuizFacade quizFacade) {
        this.quizFacade = quizFacade;
    }

    public QuestionFacade getQuestionFacade() {
        return questionFacade;
    }

    public void setQuestionFacade(QuestionFacade questionFacade) {
        this.questionFacade = questionFacade;
    }

    public AnswerFacade getAnswerFacade() {
        return answerFacade;
    }

    public void setAnswerFacade(AnswerFacade answerFacade) {
        this.answerFacade = answerFacade;
    }

    public String saveQuiz(String quizTitle, String quizTime, List<QuizQuestion> questions){
//        (String title, boolean publish, Date publishAt, int timeLimit, int userID, String accessCode)
        User signedInUser = (User) Methods.sessionMap().get("user");
        String access_code = accessCode;
        Quiz quizEntity = new Quiz(quizTitle, false, new Date(), Integer.parseInt(quizTime), signedInUser.getId(), access_code);
        quizFacade.create(quizEntity);
        Quiz found = quizFacade.findOneQuiz(access_code);
        int quizID = found.getId();
//        public Question(String questionText, Integer questionPoint, Integer quizID) {
        for (int i = 0; i < questions.size(); i++){
            QuizQuestion qq = questions.get(i);
            Question questionEntity = new Question(qq.getQuestionText(), qq.getQuestionPoint(), quizID);
            questionFacade.create(questionEntity);
            Question foundQuestion = questionFacade.findAllquestions(quizID).get(i);
            int questionID = foundQuestion.getId();
            List<AnswerChoice> choices = qq.getAnswerChoices();
            for (int j = 0; j < choices.size(); j++){
                AnswerChoice answerChoice = choices.get(j);
                Answer answerEntity = new Answer(answerChoice.getAnswerText(), answerChoice.getCorrect(), questionID);
                answerFacade.create(answerEntity);
            }
        }
        return "/quizzes/MyQuizzes?faces-redirect=true";
    }

    public String prepareEdit(int id) {
        clear();
        List<Question> questionItems = getQuestionFacade().findQuestionByQuizId(id);
        Quiz selectedQuiz = getQuizFacade().findQuizByID(id);
        List<Answer>  answerItems;
        for(int i = 0; i < questionItems.size(); i++) {
            answerItems = getAnswerFacade().findAllAnswersForOneQuestion(questionItems.get(i).getId());
            ArrayList<AnswerChoice> everyAnswer = new ArrayList<>();
            for(int j = 0; j < answerItems.size(); j++) {
                everyAnswer.add(new AnswerChoice(answerItems.get(j).getAnswer_text(), answerItems.get(j).isInstructorResult(), getCharForNumber(j + 1),i, answerItems.get(j).getId(), false));
            }
            questions.add(new QuizQuestion(questionItems.get(i).getQuestionText(), questionItems.get(i).getQuestionPoint(), i+1, everyAnswer));
        }
        quizTitle = selectedQuiz.getTitle();
        quizTime = String.valueOf(selectedQuiz.getTimeLimit());
        accessCode = selectedQuiz.getAccessCode();
        if (questionItems.size() == 0){
            questionNumber = 1;
        }else{
            questionNumber = questionItems.size() + 1;
        }
        return "/quizzes/EditQuiz?faces-redirect=true";
    }
    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
    }

    public void clearAllQuestions() {
        this.questions = new ArrayList<QuizQuestion>();
        this.selectedAnswerChoices = new ArrayList<AnswerChoice>();
        questionNumber = 1;
    }

    public void addAnswerChoice() {
        selectedAnswerChoices.add(new AnswerChoice("", false,"A",1,1, false));
        PrimeFaces.current().ajax().update("QuizEditQuestionForm:manage-question-content", "EditQuizForm:dt-questions");
    }

    public void addAnswerChoiceByQuestion() {
        selectedQuestion.getAnswerChoices().add(new AnswerChoice("", false,"A",1,1, false));
        PrimeFaces.current().ajax().update("QuizEditQuestionForm:manage-question-content", "EditQuizForm:dt-questions");
    }

    public void editSelectedQuestion(){
        clearQuestion();
        PrimeFaces.current().ajax().update("EditQuizForm:messages", "EditQuizForm:dt-questions");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("You have successfully edited your question"));
        PrimeFaces.current().executeScript("PF('editQuestionDialog').hide()");
        PrimeFaces.current().ajax().update("EditQuizForm:messages");
    }

    public void deleteSelectedQuestion() {
        this.questions.remove(this.selectedQuestion);
        this.selectedQuestion = null;
        questionNumber--;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Question Removed"));
        PrimeFaces.current().ajax().update("EditQuizForm:messages", "EditQuizForm:dt-questions");
    }

    public void deleteSelectedAnswerChoice(AnswerChoice choice) {
        this.selectedAnswerChoices.remove(choice);
        PrimeFaces.current().ajax().update("QuizAddForm:manage-question-content", "QuizAddForm:manage-question-content");
    }

    public void deleteSelectedAnswerChoiceByQuestion(AnswerChoice choice) {
        this.selectedQuestion.getAnswerChoices().remove(choice);
        PrimeFaces.current().ajax().update("QuizEditQuestionForm:manage-question-content");
    }

    public void createQuestion(){
        this.selectedQuestion = new QuizQuestion(questionTitle, questionPoint, questionNumber, this.selectedAnswerChoices);
        this.questions.add(this.selectedQuestion);
        questionNumber++;
        clearQuestion();
        PrimeFaces.current().ajax().update("EditQuizForm:messages", "EditQuizForm:dt-questions");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Question Added"));
        PrimeFaces.current().executeScript("PF('manageQuestionDialog').hide()");
        PrimeFaces.current().ajax().update("EditQuizForm:messages");
    }

    public void clearQuestion(){
        questionTitle = null;
        questionPoint = 1;
        selectedAnswerChoices = new ArrayList<AnswerChoice>();
    }

    public void clear() {
        quizTime = null;
        quizTitle = null;
        clearAllQuestions();
        clearQuestion();
        accessCode = null;
    }
}
