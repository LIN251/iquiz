package edu.vt.controllers;


import edu.vt.EntityBeans.Answer;
import edu.vt.EntityBeans.Question;
import edu.vt.EntityBeans.Quiz;
import edu.vt.FacadeBeans.AnswerFacade;
import edu.vt.FacadeBeans.QuestionFacade;
import edu.vt.FacadeBeans.QuizFacade;
import edu.vt.pojo.AnswerChoice;
import edu.vt.pojo.QuizQuestion;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
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

    private List<Quiz> items = null;
    private List<Question> questionItems = null;
    private List<Answer> answerItems = null;
    private ArrayList<QuizQuestion> quizQuestions;
    private Quiz selected;
    private Quiz selectedQuiz = null;

    private List<Integer> did;
    /*
    ==================
    Constructor Method
    ==================
    */
    public MyQuizController() {
        quizQuestions = new ArrayList<QuizQuestion>();
    }

    public QuizFacade getQuizFacade() { return quizFacade; }

    public void setQuizFacade(QuizFacade quizFacade) { this.quizFacade = quizFacade; }

    public QuestionFacade getQuestionFacade() { return questionFacade; }

    public void setQuestionFacade(QuestionFacade questionFacade) { this.questionFacade = questionFacade; }

    public AnswerFacade getAnswerFacade() { return answerFacade; }

    public void setAnswerFacade( AnswerFacade answerFacade) { this.answerFacade = answerFacade; }

    public Quiz getSelected() { return selected; }

    public void setSelected(Quiz selected) { this.selected = selected; }

    public List<Quiz> getItems() {
        items = null;
        if (items == null) {
            items = getQuizFacade().findAll();
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

    public String updateQuestion(int id) {

//        System.out.println("0000000000");
//        System.out.println(id);
        questionItems = null;
        selectedQuiz = null;
        answerItems = null;
        quizQuestions.clear();

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
                    everyAnswer.add(new AnswerChoice(answerItems.get(j).getAnswer_text(), false, getCharForNumber(j + 1)));
                }
                quizQuestions.add(new QuizQuestion(questionItems.get(i).getQuestionText(), questionItems.get(i).getQuestionPoint(), questionItems.get(i).getId(), everyAnswer));

            }
        }
        return "/quizzes/MyQuizzesView?faces-redirect=true";
    }

    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
    }

}
