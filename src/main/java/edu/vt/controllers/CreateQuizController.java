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

@Named("createQuizController")

/*
The @SessionScoped annotation preserves the values of the UserController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped
public class CreateQuizController implements Serializable{
    @EJB
    private QuizFacade quizFacade;

    @EJB
    private QuestionFacade questionFacade;

    @EJB
    private AnswerFacade answerFacade;

    public CreateQuizController() {

    }

    public void saveQuiz(String quizTitle, String quizTime, List<QuizQuestion> questions){
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
            Question questionEntity = new Question(qq.getQuestionText(), qq.getQuestionNumber(), quizID);
            questionFacade.create(questionEntity);
            Question foundQuestion = questionFacade.findAllquestions(quizID).get(0);
            int questionID = foundQuestion.getId();
            List<AnswerChoice> choices = qq.getAnswerChoices();
            for (int j = 0; j < choices.size(); j++){
                AnswerChoice answerChoice = choices.get(j);
                Answer answerEntity = new Answer(answerChoice.getAnswerText(), answerChoice.getCorrect(), questionID);
                answerFacade.create(answerEntity);
            }
        }
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
