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

@Named("apiController")
/*
The @SessionScoped annotation preserves the values of the RecipeController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped
public class ApiController implements Serializable {

    private String searchApiUrl;
    private List<QuizQuestion> questions;

    public ApiController() {
        questions = new ArrayList<>();
    }

    public void performSearch() {

        //Clear the list at the start of search
        questions.clear();

        //Format the apiUrl
        searchApiUrl = "https://opentdb.com/api.php?amount=10&category=18&type=multiple";
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
                String correctAnswer = aQuestion.optString("correct_answer", "");
                answers.add(new AnswerChoice(correctAnswer, true, "A"));
                JSONArray incorrectAnswersArray = aQuestion.getJSONArray("incorrect_answers");
                for (int j=0; j<incorrectAnswersArray.length(); j++) {
                    String aIncorrectAnswer = incorrectAnswersArray.getString(j);
                    answers.add(new AnswerChoice(aIncorrectAnswer, false, "A"));
                }
                Collections.shuffle(answers);
                questions.add(new QuizQuestion(questionText, 1, 1, answers));
            }
        } catch (Exception e) {
            Methods.showMessage("Fatal Error", "Unrecognized Search Query!",
                    "The Recipes API provides no data for the search query entered!");
//            clear();
        }

        for (int i = 0; i < questions.size(); i++){
            System.out.println(questions.get(i).getQuestionText());
            for (int j=0; j<questions.get(i).getAnswerChoices().size(); j++){
                System.out.println(questions.get(i).getAnswerChoices().get(j).getAnswerText());
                System.out.println(questions.get(i).getAnswerChoices().get(j).getCorrect());
            }
        }
        // Reset search queries
//        searchQuery = null;
//        dietLabel = null;
//        healthLabel = null;
//        maxNumberOfRecipes = null;
        return;
    }
}
