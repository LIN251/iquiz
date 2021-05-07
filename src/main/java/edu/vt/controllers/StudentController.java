/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
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

/*
---------------------------------------------------------------------------
The @Named (javax.inject.Named) annotation indicates that the objects
instantiated from this class will be managed by the Contexts and Dependency
Injection (CDI) container. The name "studentController" is used within
Expression Language (EL) expressions in JSF (XHTML) facelets pages to
access the properties and invoke methods of this class.
---------------------------------------------------------------------------
 */
@Named("studentController")

/*
The @SessionScoped annotation preserves the values of the StudentController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped

/*
--------------------------------------------------------------------------
Marking the StudentController class as "implements Serializable" implies that
instances of the class can be automatically serialized and deserialized.

Serialization is the process of converting a class instance (object)
from memory into a suitable format for storage in a file or memory buffer,
or for transmission across a network connection link.

Deserialization is the process of recreating a class instance (object)
in memory from the format under which it was stored.
--------------------------------------------------------------------------
 */
public class StudentController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================

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
    The @EJB annotation directs the storage (injection) of the object
    reference of the JPA TakerFacade class object into the instance
    variable TakerFacade below after it is instantiated at runtime.
    */
    @EJB
    private TakerFacade takerFacade;

    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private List<Student> students;
    private Quiz quiz = null;
    private Integer score = 0;
    private Quiz innerquiz = null;
    private Integer innerscore = 0;
    private List<Question> innerquestion = null;
    private ArrayList<QuizQuestion> quizQuestions;
    private String innername = null;
    private List<Student> filteredStudents;

    /*
    ==================
    Constructor Method
    ==================
     */
    public StudentController() {
        students = new ArrayList<>();
        quizQuestions = new ArrayList<QuizQuestion>();
    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
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

    public List<Student> getFilteredStudents() { return filteredStudents; }

    public void setFilteredStudents(List<Student> filteredStudents) { this.filteredStudents = filteredStudents; }

    /**
     * lists all taker
     * @param quizId lists all taker in this quiz id
     * @return the student list page address
     */
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

    /**
     * see the specific taker answer choice
     * @param quizId the quiz id
     * @param takerID the taker are ready to see
     * @return
     */
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

    /**
     * Convert the number to a b c d format
     * @param i the number waiting for convert
     * @return the a b c d format
     */
    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
    }
}