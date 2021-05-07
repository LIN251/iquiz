
/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
package edu.vt.controllers;
import edu.vt.EntityBeans.*;
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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/*
---------------------------------------------------------------------------
The @Named (javax.inject.Named) annotation indicates that the objects
instantiated from this class will be managed by the Contexts and Dependency
Injection (CDI) container. The name "createQuizController" is used within
Expression Language (EL) expressions in JSF (XHTML) facelets pages to
access the properties and invoke methods of this class.
---------------------------------------------------------------------------
 */
@Named("createQuizController")

/*
The @SessionScoped annotation preserves the values of the CreateQuizController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped

/*
--------------------------------------------------------------------------
Marking the CreateQuizController class as "implements Serializable" implies that
instances of the class can be automatically serialized and deserialized.

Serialization is the process of converting a class instance (object)
from memory into a suitable format for storage in a file or memory buffer,
or for transmission across a network connection link.

Deserialization is the process of recreating a class instance (object)
in memory from the format under which it was stored.
--------------------------------------------------------------------------
 */
public class CreateQuizController implements Serializable{
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
    ==================
    Constructor Method
    ==================
     */
    public CreateQuizController() {

    }

    /**
     * Create the quiz when clicking the submit button
     * @param quizTitle the quiz title are ready to add to the database
     * @param questions the questions are ready to add to the database
     * @param quizTime the quiz times are ready to add to the database
     * @return go to the MyQuizzes page when finish creating quiz
     */
    public String saveQuiz(String quizTitle, String quizTime, List<QuizQuestion> questions){
//        (String title, boolean publish, Date publishAt, int timeLimit, int userID, String accessCode)
        User signedInUser = (User) Methods.sessionMap().get("user");
        String access_code = randomAccessCode();
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

    /**
     * Edit the quiz when clicking the edit quiz button
     * @param quizTitle the quiz title are ready to update to the database
     * @param questions the questions are ready to update to the database
     * @param quizTime the quiz times are ready to update to the database
     * @param access_code the access_code are called to find the quiz
     * @return go to the MyQuizzes page when finish editing quiz
     */
    public String editQuiz(String quizTitle, String quizTime, List<QuizQuestion> questions, String access_code){
//        (String title, boolean publish, Date publishAt, int timeLimit, int userID, String accessCode)
        User signedInUser = (User) Methods.sessionMap().get("user");
        Quiz quizEntity = quizFacade.findOneQuiz(access_code);
        quizEntity.setTitle(quizTitle);
        quizEntity.setTimeLimit(Integer.parseInt(quizTime));
        quizFacade.edit(quizEntity);
        int quizID = quizEntity.getId();
        deleteQuestionAndAnswersForQuiz(quizID);
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

    /**
     * Remove the questions and answers from database
     * @param quizID the quiz id and this quiz's question and answer need to remove
     */
    public void deleteQuestionAndAnswersForQuiz(int quizID) {
        List<Question> questions = questionFacade.findQuestionByQuizId(quizID);
        for(int i = 0; i < questions.size(); i++) {
            List<Answer> answers = answerFacade.findAllAnswersForOneQuestion(questions.get(i).getId());
            for(int j = 0; j < answers.size(); j++) {
                answerFacade.remove(answers.get(j));
            }
            questionFacade.remove(questions.get(i));
        }
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
