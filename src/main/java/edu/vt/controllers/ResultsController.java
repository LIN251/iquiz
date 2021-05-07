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
Injection (CDI) container. The name "resultsController" is used within
Expression Language (EL) expressions in JSF (XHTML) facelets pages to
access the properties and invoke methods of this class.
---------------------------------------------------------------------------
 */
@Named("resultsController")

/*
The @SessionScoped annotation preserves the values of the ResultsController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped

/*
--------------------------------------------------------------------------
Marking the ResultsController class as "implements Serializable" implies that
instances of the class can be automatically serialized and deserialized.

Serialization is the process of converting a class instance (object)
from memory into a suitable format for storage in a file or memory buffer,
or for transmission across a network connection link.

Deserialization is the process of recreating a class instance (object)
in memory from the format under which it was stored.
--------------------------------------------------------------------------
 */
public class ResultsController implements Serializable {
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

    private BarChartModel overallChart;

    private BarChartModel answerChart;


    @PostConstruct
    public void init() {
        // Display default stock chart
        overallChart = new BarChartModel();
        answerChart = new BarChartModel();
    }

    public void setupStockChart() {
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

    public BarChartModel getAnswerChart() { return answerChart; }

    public BarChartModel getOverallChart() {
        return overallChart;
    }


    /**
     * get all the necessary data waiting for bar chart use
     * @param quizId the quiz id are ready to get the inside information
     */
    public void obtainOverallData(int quizId) {
        Quiz quiz = getQuizFacade().findQuizByID(quizId);
        List<Question> questionList = getQuestionFacade().findQuestionByQuizId(quiz.getId());
        String[] xlabel = new String[questionList.size()];
        Integer[] ylabel = new Integer[questionList.size()];
        for(int i = 0; i < questionList.size(); i++) {
            xlabel[i] = "Q" + (i + 1);
            List<Answer> answerList = getAnswerFacade().findAllAnswersForOneQuestion(questionList.get(i).getId());
            int rightAnswerID = 0;
            for(int j = 0; j < answerList.size(); j++) {
                if(answerList.get(j).isInstructorResult()) {
                    rightAnswerID = answerList.get(j).getId();
                }
            }
            List<AttemptAnswer> attemptAnswerList = getAttemptAnswerFacade().findAttemptAnswerByAnswerId(rightAnswerID);
            ylabel[i] = attemptAnswerList.size();
        }
        createChartModel(quiz.getId(), ylabel, xlabel, quiz.getTitle());
    }

    /**
     * get the necessary data in a specific question
     * @param questionId the question id are ready to get inside information
     */
    public void obtainAnswerChartData(int questionId) {
        Question question = getQuestionFacade().findQuestionByQuestionId(questionId);
        List<Answer> answerList = getAnswerFacade().findAllAnswersForOneQuestion(question.getId());
        String[] xlabel = new String[answerList.size()];
        Integer[] ylabel = new Integer[answerList.size()];
        for(int i = 0; i < answerList.size(); i++) {
            List<AttemptAnswer> attemptAnswerList = getAttemptAnswerFacade().findAttemptAnswerByAnswerId(answerList.get(i).getId());
            ylabel[i] = attemptAnswerList.size();
            if(answerList.get(i).isInstructorResult()) {
                xlabel[i] = answerList.get(i).getAnswer_text() + " (CORRECT) ";
            }
            else {
                xlabel[i] = answerList.get(i).getAnswer_text();
            }
        }
        createAnswerChartModel(xlabel, ylabel, question.getQuestionText());
    }

    /**
     * create a overall bar chart
     * @param id unused id
     * @param ylabel the ylabel list
     * @param xlabel the xlabel list
     * @param quiztitle the quiz title
     */
    public void createChartModel(Integer id, Integer[] ylabel, String[] xlabel, String quiztitle) {
//        overallChart = new BarChartModel();
        overallChart.clear();
        ChartSeries series = new ChartSeries();
        series.setLabel("Question/Correct");
        for(int i = 0; i < xlabel.length; i++) {
            series.set(xlabel[i], ylabel[i]);
        }
        overallChart.addSeries(series);
        overallChart.setLegendPosition("ne");
        Axis xAxis = overallChart.getAxis(AxisType.X);
        xAxis.setLabel(quiztitle);
        Axis yAxis = overallChart.getAxis(AxisType.Y);
        yAxis.setLabel("Correct Number");
        yAxis.setMin(0);
        overallChart.setExtender("barChartExtender");
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('ReportDialog').show();");
    }

    /**
     * create a bar chart for an answer
     * @param xlabel xlabel list
     * @param ylabel ylabel list
     * @param questionTitle the quiz title
     */
    private void createAnswerChartModel(String[] xlabel, Integer[] ylabel, String questionTitle) {
        answerChart.clear();
        ChartSeries series = new ChartSeries();
        series.setLabel("Answer/Correct");
        for(int i = 0; i < xlabel.length; i++) {
            series.set(xlabel[i], ylabel[i]);
        }
        answerChart.addSeries(series);
        answerChart.setLegendPosition("ne");
        Axis xAxis = answerChart.getAxis(AxisType.X);
        xAxis.setLabel(questionTitle);
        Axis yAxis = answerChart.getAxis(AxisType.Y);
        yAxis.setLabel("Correct Number");
        yAxis.setMin(0);
        answerChart.setExtender("barChartExtender");
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('AnswerReportDialog').show();");
    }
}
