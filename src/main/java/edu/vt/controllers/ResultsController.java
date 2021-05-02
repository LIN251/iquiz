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

@Named("resultsController")

/*
The @SessionScoped annotation preserves the values of the CountryController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped

public class ResultsController implements Serializable {

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

    private List<BarChartModel> chartList;

    private BarChartModel overallChart;


    @PostConstruct
    public void init() {
        // Display default stock chart
        chartList = new ArrayList<>();
        overallChart = new BarChartModel();
    }

    public void setupStockChart() {
        obtainCharData();
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

    public List<BarChartModel> getChartList() { return chartList; }

    public void setChartList(List<BarChartModel> chartList) { this.chartList = chartList; }

    public BarChartModel getOverallChart() {
        return overallChart;
    }


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
    private void obtainCharData() {
        User signedInUser = (User) Methods.sessionMap().get("user");
        List<Quiz> quizList = getQuizFacade().findQuizByUserId(signedInUser.getId());
        for(int i = 0; i < quizList.size(); i++) {
            List<Question> questionList = getQuestionFacade().findQuestionByQuizId(quizList.get(i).getId());
            for(int j = 0; j < questionList.size(); j++) {
                List<Answer> answerList = getAnswerFacade().findAllAnswersForOneQuestion(questionList.get(j).getId());
                Integer[] count = new Integer[answerList.size()];
                String[] answerLabel = new String[answerList.size()];
                for(int k = 0; k < answerList.size(); k++) {
                    List<AttemptAnswer> attemptAnswerList = getAttemptAnswerFacade().findAttemptAnswerByAnswerId(answerList.get(k).getId());
                    count[k] = attemptAnswerList.size();
                    if(answerList.get(k).isInstructorResult()) {
                        String res = answerList.get(k).getAnswer_text() + " (CORRECT) ";
                        answerLabel[k] = res;
                    }
                    else {
                        answerLabel[k] = answerList.get(k).getAnswer_text();
                    }
                }
                createCharModel("Question" + (j+1), count, questionList.get(j).getQuestionText(), answerLabel);
            }
        }
    }

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

    private void createCharModel(String label, Integer[] count, String question, String[] answerLabel) {
        BarChartModel chart = new BarChartModel();
        ChartSeries series = new ChartSeries();
        series.setLabel(label);
        for(int i = 0; i < count.length; i++) {
            series.set(answerLabel[i], count[i]);
        }
        chart.addSeries(series);
        chart.setLegendPosition("ne");
        Axis xAxis = chart.getAxis(AxisType.X);
        xAxis.setLabel(question);
        Axis yAxis = chart.getAxis(AxisType.Y);
        yAxis.setLabel("Selected Answer Number");
        yAxis.setMin(0);
        chart.setExtender("barChartExtender");
        chartList.add(chart);
    }

//    private String getCharForNumber(int i) {
//        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
//    }
}
