
/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.globals.Methods;
import edu.vt.pojo.AnswerChoice;
import edu.vt.pojo.QuizQuestion;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.text.StringEscapeUtils;

/*
---------------------------------------------------------------------------
The @Named (javax.inject.Named) annotation indicates that the objects
instantiated from this class will be managed by the Contexts and Dependency
Injection (CDI) container. The name "apiController" is used within
Expression Language (EL) expressions in JSF (XHTML) facelets pages to
access the properties and invoke methods of this class.
---------------------------------------------------------------------------
 */
@Named("apiController")
/*
The @SessionScoped annotation preserves the values of the ApiController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped

/*
--------------------------------------------------------------------------
Marking the ApiController class as "implements Serializable" implies that
instances of the class can be automatically serialized and deserialized.

Serialization is the process of converting a class instance (object)
from memory into a suitable format for storage in a file or memory buffer,
or for transmission across a network connection link.

Deserialization is the process of recreating a class instance (object)
in memory from the format under which it was stored.
--------------------------------------------------------------------------
 */
public class ApiController implements Serializable {
    /*
   ===============================
   Instance Variables (Properties)
   ===============================
   */
    private String searchApiUrl;
    private List<QuizQuestion> questions;
    private AnswerChoice selectedAns;
    private int totalCorrect = 0;
    private String category;
    private String numberOfQuestions = "10";
    private String difficulty;
    private String type;

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public ApiController() {
        questions = new ArrayList<>();
    }

    public String getSearchApiUrl() {
        return searchApiUrl;
    }

    public void setSearchApiUrl(String searchApiUrl) {
        this.searchApiUrl = searchApiUrl;
    }

    public List<QuizQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuizQuestion> questions) {
        this.questions = questions;
    }

    public AnswerChoice getSelectedAns() {
        return selectedAns;
    }

    public void setSelectedAns(AnswerChoice selectedAns) {
        this.selectedAns = selectedAns;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(String numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotalCorrect() {
        return totalCorrect;
    }

    public void setTotalCorrect(int totalCorrect) {
        this.totalCorrect = totalCorrect;
    }

    /**
     * Grab the api and get the information
     * @return the address of take trivia quiz
     */
    public String performSearch() {

        //Clear the list at the start of search
        questions.clear();
        if (Integer.parseInt(numberOfQuestions) > 50) {
            return null;
        }
        //Format the apiUrl
        searchApiUrl = "https://opentdb.com/api.php?amount=" + numberOfQuestions;
//        &category=18&type=multiple"
        if (!category.equals("Any Category")){
            searchApiUrl += "&category="+categoryNumberFromString(category);
        }
        if(difficulty != null && difficulty.equals("") ){
            searchApiUrl += "&" + difficulty;
        }
        if (type != null && !type.equals("")){
            searchApiUrl += "&" + type;
        }
        /*
        Redirecting to show a JSF page involves more than one subsequent requests and
        the messages would die from one request to another if not kept in the Flash scope.
        Since we will redirect to show the search Results page, we invoke preserveMessages().
         */
        Methods.preserveMessages();

        /*
        JSON uses the following notation:
        { }    represents a JavaScript object as a Dictionary with Key:Value pairs
        [ ]    represents Array
        [{ }]  represents an Array of JavaScript objects (dictionaries)
          :    separates Key from the Value
         */
        try {
            // Obtain the JSON file from the searchApiUrl
            String searchResultsJsonData = Methods.readUrlContent(searchApiUrl);
            JSONObject openTriviaJsonObject;

            openTriviaJsonObject = new JSONObject(searchResultsJsonData);

            //Turn hits into an Array
            JSONArray resultsArray = openTriviaJsonObject.getJSONArray("results");

            //Process each element in the hits array
            for(int i=0; i < resultsArray.length(); i++){
                List<AnswerChoice> answers = new ArrayList<>();
                JSONObject aQuestion = resultsArray.getJSONObject(i);
                String questionText;
                //Getting the question
                questionText = aQuestion.optString("question", "");
                if (questionText.equals("")){
                    questionText = "Question Text Unavailable";
                }
                questionText = StringEscapeUtils.unescapeHtml4(questionText);
                String correctAnswer = aQuestion.optString("correct_answer", "");
                correctAnswer = StringEscapeUtils.unescapeHtml4(correctAnswer);
                answers.add(new AnswerChoice(correctAnswer, true, "A", 0, 0, false));
                JSONArray incorrectAnswersArray = aQuestion.getJSONArray("incorrect_answers");
                for (int j=0; j<incorrectAnswersArray.length(); j++) {
                    String aIncorrectAnswer = incorrectAnswersArray.getString(j);
                    aIncorrectAnswer = StringEscapeUtils.unescapeHtml4(aIncorrectAnswer);
                    answers.add(new AnswerChoice(aIncorrectAnswer, false, getCharForNumber(j+2), 0, 0, false));
                }
                Collections.shuffle(answers);
                questions.add(new QuizQuestion(1,questionText, 1, 1, answers));
            }
        } catch (Exception e) {
            Methods.showMessage("Fatal Error", "Unrecognized Search Query!",
                    "The Recipes API provides no data for the search query entered!");
            clear();
        }
        // Reset search queries
        clear();
        return "/api/TakeOpenTriviaQuiz?faces-redirect=true";
    }

    /**
     * see if the student's answer is correct
     * @return the answer choice
     */
    public AnswerChoice studentCorrectAnswer(List<AnswerChoice> choices) {
        for (int i = 0; i < choices.size(); i++) {
            if (choices.get(i).getStudentCorrect()){
                return choices.get(i);
            }
        }
        return null;
    }

    /**
     * reset all
     */
    public void clear(){
        category = null;
        selectedAns = null;
        numberOfQuestions = "10";
        difficulty = null;
        type = null;
        totalCorrect = 0;
    }

    /**
     * Select the row when students taking the trivia quiz
     */
    public void onRowSelect(QuizQuestion question) {
        int questionIndex = questions.indexOf(question);
        int answerIndex = questions.get(questionIndex).getAnswerChoices().indexOf(selectedAns);
        for(int i = 0; i < questions.get(questionIndex).getAnswerChoices().size(); i++) {
            questions.get(questionIndex).getAnswerChoices().get(i).setStudentCorrect(false);
        }
        questions.get(questionIndex).getAnswerChoices().get(answerIndex).setStudentCorrect(true);
    }

    /**
     * Submit the quiz when student click submit button
     * @return the results page of trivia quiz
     */
    public String submitQuiz() {
        for(int i = 0; i < questions.size(); i++) {
            QuizQuestion aQuestion = questions.get(i);
            List<AnswerChoice> choices = aQuestion.getAnswerChoices();
            AnswerChoice correctAnswerChoice = null;
            for (int j = 0; j < choices.size(); j++) {
                if (choices.get(j).getCorrect() && choices.get(j).getStudentCorrect()){
                    totalCorrect++;
                }
            }
        }
        return "/api/TriviaQuizResult?faces-redirect=true";
    }

    /**
     * return the number from category string
     * @param category the category string
     * @return the string returned
     */
    public String categoryNumberFromString(String category) {
        switch (category) {
            case "Any Category":
                return "";
            case "General Knowledge":
                return "9";
            case "Entertainment:Books":
                return "10";
            case "Entertainment:Film":
                return "11";
            case "Entertainment:Music":
                return "12";
            case "Entertainment:MusicalTheatres":
                return "13";
            case "Entertainment:Television":
                return "14";
            case "Entertainment:Video Games":
                return "15";
            case "Entertainment:Board Games":
                return "16";
            case "ScienceNature":
                return "17";
            case "Science: Computers":
                return "18";
            case "Science: Mathematics":
                return "19";
            case "Mythology":
                return "20";
            case "Sports":
                return "21";
            case "Geography":
                return "22";
            case "History":
                return "23";
            case "Politics":
                return "24";
            case "Art":
                return "25";
            case "Celebrities":
                return "26";
            case "Animals":
                return "27";
            case "Vehicles":
                return "28";
            case "Entertainment:Comics":
                return "29";
            case "Science:Gadgets":
                return "30";
            case "Entertainment:JapaneseAnimeManga":
                return "31";
            case "Entertainment:CartoonAnimations":
                return "32";
        }
        return "";
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
