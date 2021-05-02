package edu.vt.controllers;

/*
---------------------------------------------------------------------------
The @Named (javax.inject.Named) annotation indicates that the objects
instantiated from this class will be managed by the Contexts and Dependency
Injection (CDI) container. The name "recipeController" is used within
Expression Language (EL) expressions in JSF (XHTML) facelets pages to
access the properties and invoke methods of this class.
---------------------------------------------------------------------------
 */

import edu.vt.EntityBeans.*;
import edu.vt.FacadeBeans.*;
import edu.vt.globals.Methods;
import edu.vt.pojo.AnswerChoice;
import edu.vt.pojo.QuizQuestion;
import edu.vt.pojo.Student;
import org.primefaces.PrimeFaces;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Named("studentController")

/*
The @SessionScoped annotation preserves the values of the CountryController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped

public class StudentController implements Serializable {

    @EJB
    private AttemptFacade attemptFacade;

    @EJB
    private AttemptAnswerFacade attemptAnswerFacade;

    @EJB
    private QuizFacade quizFacade;

    @EJB
    private QuestionFacade questionFacade;

    @EJB
    private AnswerFacade answerFacade;

    @EJB
    private TakerFacade takerFacade;

    private List<Student> students;

    private Quiz quiz = null;

    private Integer score = 0;

    private Quiz innerquiz = null;

    private Integer innerscore = 0;

    private List<Question> innerquestion = null;

    private ArrayList<QuizQuestion> quizQuestions;

    private String innername = null;



    public StudentController() {
        students = new ArrayList<>();
        quizQuestions = new ArrayList<QuizQuestion>();
    }

    public AttemptFacade getAttemptFacade() { return attemptFacade; }

    public void setAttemptFacade(AttemptFacade attemptFacade) { this.attemptFacade = attemptFacade; }

    public AttemptAnswerFacade getAttemptAnswerFacade() { return attemptAnswerFacade; }

    public void setAttemptAnswerFacade(AttemptAnswerFacade attemptAnswerFacade) { this.attemptAnswerFacade = attemptAnswerFacade; }

    public QuizFacade getQuizFacade() { return quizFacade; }

    public void setQuizFacade(QuizFacade quizFacade) { this.quizFacade = quizFacade; }

    public QuestionFacade getQuestionFacade() { return questionFacade; }

    public void setQuestionFacade(QuestionFacade questionFacade) { this.questionFacade = questionFacade; }

    public AnswerFacade getAnswerFacade() { return answerFacade; }

    public void setAnswerFacade(AnswerFacade answerFacade) { this.answerFacade = answerFacade; }

    public TakerFacade getTakerFacade() { return takerFacade; }

    public void setTakerFacade(TakerFacade takerFacade) { this.takerFacade = takerFacade; }

    public List<Student> getStudents() { return students; }

    public Quiz getQuiz() { return quiz; }

    public Integer getScore() { return score; }

    public Quiz getInnerquiz() { return innerquiz; }

    public Integer getInnerscore() { return innerscore; }

    public List<Question> getInnerquestion() { return innerquestion; }

    public ArrayList<QuizQuestion> getQuizQuestions() { return quizQuestions; }

    public String getInnername() { return innername; }



    public String listStudents(int quizId) {
        quiz = null;
        score = 0;
        if(quiz == null) {
            quiz = getQuizFacade().findQuizByID(quizId);
            List<Question> questions = getQuestionFacade().findQuestionByQuizId(quizId);
            for(int i = 0; i < questions.size(); i++) {
                score += questions.get(i).getQuestionPoint();
            }
        }
        students.clear();
        List<Attempt> attempts = new ArrayList<>();
        attempts = getAttemptFacade().findAllAttemptByQuizId(quizId);
        for(int i = 0; i < attempts.size(); i++) {
            System.out.println(attempts.get(i).getTakerId());
            Taker taker = takerFacade.findTakerById(attempts.get(i).getTakerId());
            Student student = new Student(taker.getId(), taker.getFirstName(), attempts.get(i).getScore());
            students.add(student);
        }
        return "/quizzes/StudentList?faces-redirect=true";
    }

    public String studentSubmission(int quizId, int takerID) {
        innerquiz = null;
        innerquestion = null;
        innername = null;
        innerscore = 0;
        quizQuestions.clear();
        if(innername == null) {
            Taker taker = getTakerFacade().findTakerById(takerID);
            innername = taker.getFirstName();
        }
        if(innerquiz == null) {
            innerquiz = getQuizFacade().findQuizByID(quizId);
            innerquestion = getQuestionFacade().findQuestionByQuizId(quizId);
            List<Attempt> attempts = getAttemptFacade().findAttemptByTakerId(takerID);
            Attempt attempt = attempts.get(0);
            List<AttemptAnswer> attemptAnswer = getAttemptAnswerFacade().findAttemptAnswerByAttemptId(attempt.getId());
            for(int i = 0; i < innerquestion.size(); i++) {
                innerscore += innerquestion.get(i).getQuestionPoint();
                List<Answer> answerList = getAnswerFacade().findAllAnswersForOneQuestion(innerquestion.get(i).getId());
                ArrayList<AnswerChoice> everyAnswer = new ArrayList<AnswerChoice>();
                int id = -1;
                for(int k = 0; k < attemptAnswer.size(); k++) {
                    if(innerquestion.get(i).getId() == attemptAnswer.get(k).getQuestionID()) {
                        id = attemptAnswer.get(k).getAnswerID();
                    }
                }
                for(int j = 0; j < answerList.size(); j++) {
                    if( id == answerList.get(j).getId()) {
                        everyAnswer.add(new AnswerChoice(answerList.get(j).getAnswer_text(), answerList.get(j).isInstructorResult(), getCharForNumber(j + 1),i,answerList.get(j).getId(), true));
                    }
                    else {
                        everyAnswer.add(new AnswerChoice(answerList.get(j).getAnswer_text(), answerList.get(j).isInstructorResult(), getCharForNumber(j + 1),i,answerList.get(j).getId(), false));
                    }

                }
                quizQuestions.add(new QuizQuestion(innerquestion.get(i).getId(),innerquestion.get(i).getQuestionText(), innerquestion.get(i).getQuestionPoint(), innerquestion.get(i).getId(), everyAnswer));
            }
        }
        return "/quizzes/StudentSubmission?faces-redirect=true";
    }

    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
    }
}
