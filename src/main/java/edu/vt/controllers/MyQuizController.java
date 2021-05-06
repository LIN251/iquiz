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
Injection (CDI) container. The name "recipeController" is used within
Expression Language (EL) expressions in JSF (XHTML) facelets pages to
access the properties and invoke methods of this class.
---------------------------------------------------------------------------
 */
@Named("myQuizController")

/*
The @SessionScoped annotation preserves the values of the CountryController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped

public class MyQuizController implements Serializable {

    /*
    ===============================
    Instance Variables (Properties)
    ===============================

    The @EJB annotation directs the storage (injection) of the object
    reference of the JPA RecipeFacade class object into the instance
    variable recipeFacade below after it is instantiated at runtime.
    */
    @EJB
    private QuizFacade quizFacade;
    @EJB
    private QuestionFacade questionFacade;
    @EJB
    private AnswerFacade answerFacade;
    @EJB
    private AttemptFacade attemptFacade;
    @EJB
    private AttemptAnswerFacade attemptAnswerFacade;

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


  public String getRandomString() {
      Random rand = new Random();
      int upperbound = 300;
      int int_random = rand.nextInt(upperbound);
      return "haha" + int_random;
  }
//    public List<Question> getQuestionItems(int id) {
//        if(questionItems == null) {
//            questionItems = getQuestionFacade().findQuestionByQuizId(id);
//        }
//        return questionItems;
//    }

    public List<Question> getQuestionItems() {
        return questionItems;
    }

    public Quiz getSelectedQuiz() { return selectedQuiz; }

    public List<QuizQuestion> getQuizQuestions() { return quizQuestions; }

    public Integer getTotalPoints() { return totalPoints; }

    public void setItems(List<Quiz> items) {
        this.items = items;
    }

    public int getSelectedID() {
        return selectedID;
    }

    public void setSelectedID(int selectedID) {
        this.selectedID = selectedID;
    }

    public String getSelectedAccessCode() {
        return selectedAccessCode;
    }

    public void setSelectedAccessCode(String selectedAccessCode) {
        this.selectedAccessCode = selectedAccessCode;
    }

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

    public String deleteQuiz() {
        System.out.println(selectedID);
//        List<Question> questions = getQuestionFacade().findQuestionByQuizId(quizID);
//        for(int i = 0; i < questions.size(); i++) {
//            List<Answer> answers = getAnswerFacade().findAllAnswersForOneQuestion(questions.get(i).getId());
//            for(int j = 0; j < answers.size(); j++) {
//                getAnswerFacade().remove(answers.get(j));
//            }
//            getQuestionFacade().remove(questions.get(i));
//        }
//        Quiz quiz = getQuizFacade().findQuizByID(quizID);
//        System.out.println(quiz.getTitle());
//        getQuizFacade().remove(quiz);
//        System.out.println("remove quiz");
//        addMessage("Confirmed", "This quiz has been deleted");
        return "/quizzes/MyQuizzes?faces-redirect=true";
    }

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


    public String changeDateFormat(Date date) {
        String format = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(format);
        return sdf.format(date);
    }

    public String selectedQuizLink(String accessCode) {
        String serverName = FacesContext.getCurrentInstance().getExternalContext().getRequestServerName();
        int port  = FacesContext.getCurrentInstance().getExternalContext().getRequestServerPort();
        String link = "http://" + serverName + ":" + String.valueOf(port) + "/iquiz/quizzes/AccessQuiz.xhtml?access_code=" + accessCode;
//        PrimeFaces.current().ajax().update("MyQuizzesListForm:qrLinkDia");
        return link;
    }

    public Boolean disableButton(int quizId) {
        List<Attempt> attempts = getAttemptFacade().findAllAttemptByQuizId(quizId);
        if(attempts.size() != 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
    }

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
