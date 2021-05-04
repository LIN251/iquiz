/*
 * Created by Calvin Huang on 2021.3.24
 * Copyright © 2021 Calvin Huang. All rights reserved.
 */
package edu.vt.managers;

import edu.vt.EntityBeans.User;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;
import edu.vt.globals.Password;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named(value = "passwordResetManager")
@SessionScoped

public class PasswordResetManager implements Serializable {

    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private String username;
    private String password;
    private String confirmPassword;
    private String answerToSecurityQuestion;

    /*
    The instance variable 'userFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the WildFly AS) to inject (store) the object reference
    of the UserFacade object, after it is instantiated at runtime, into the instance variable 'userFacade'.
     */
    @EJB
    private UserFacade userFacade;

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getAnswerToSecurityQuestion() {
        return answerToSecurityQuestion;
    }

    public void setAnswerToSecurityQuestion(String answerToSecurityQuestion) {
        this.answerToSecurityQuestion = answerToSecurityQuestion;
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }

    /*
    ================
    Instance Methods
    ================

    *************************************************
    Process the Username Submitted for Password Reset
    *************************************************
     */
    public String usernameSubmit() {

        // Since we will redirect to show the SecurityQuestion page, invoke preserveMessages()
        Methods.preserveMessages();

        // Obtain the object reference of the User object with username
        User user = getUserFacade().findByUsername(username);

        if (user == null) {
            Methods.showMessage("Fatal Error", "Unknown Username!",
                    "Entered username " + username + " does not exist!");
            return "";
        } else {
            // Redirect to show the SecurityQuestion page
            return "/userPasswordChange/SecurityQuestion?faces-redirect=true";
        }
    }

    /*
    ****************************************
    Return the Security Question Selected by
    the User at the Time of Account Creation
    ****************************************
     */
    public String getSelectedSecurityQuestionForUsername() {

        // Obtain the object reference of the User object with username
        User user = getUserFacade().findByUsername(username);

        // Obtain the number of the security question selected by the user
        int questionNumber = user.getSecurityQuestionNumber();

        // Return the security question corresponding to the question number
        return Constants.QUESTIONS[questionNumber];
    }

    /*
    *****************************************************
    Process the Submitted Answer to the Security Question
    *****************************************************
     */
    public String securityAnswerSubmit() throws Password.InvalidHashException, Password.CannotPerformOperationException {

        // Since we will redirect to show the ResetPassword page, invoke preserveMessages()
        Methods.preserveMessages();

        // Obtain the object reference of the User object with username
        User user = getUserFacade().findByUsername(username);

        String actualSecurityAnswer = user.getPassword();
        String enteredSecurityAnswer = getAnswerToSecurityQuestion();
        System.out.println("a!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(actualSecurityAnswer);
        if (Password.verifyPassword(enteredSecurityAnswer, actualSecurityAnswer) ) {
            /*
            Answer to the security question is correct. Redirect to show the ResetPassword page.
            */
            return "/userPasswordChange/ResetPassword?faces-redirect=true";

        } else {
            Methods.showMessage("Error", "Password Incorrect!", "");
            return "";
        }
    }

    /*
    *************************************************
    Reset Password and Redirect to Show the Home Page
    *************************************************
     */
    public String resetPassword() {

        /*
        ----------------------------------------------------------------
        Password and Confirm Password are validated under 3 conditions:
        
        <1> Non-empty (tested with required="true" in JSF page)
        <2> Correct composition satisfying the regex rule.
            (tested with <f:validator validatorId="passwordValidator" />
            in the JSF page)
        <3> Password and Confirm Password must match (tested below)
        ----------------------------------------------------------------
         */
        if (!password.equals(confirmPassword)) {
            Methods.showMessage("Fatal Error", "Unmatched Passwords!",
                    "Password and Confirm Password must Match!");
            return "";
        }

        /*
        ***************************************************
        |   Password and Confirm Password are Validated   |
        ***************************************************
        */
        // Since we will redirect to show the home page, invoke preserveMessages()
        Methods.preserveMessages();

        // Obtain the object reference of the User object with username
        User user = getUserFacade().findByUsername(username);

        try {
            //-------------------------------------------------------------------------------------
            // Convert the user-entered String password to a String containing the following parts
            //       "algorithmName":"PBKDF2_ITERATIONS":"hashSize":"salt":"hash"
            // for secure storage and retrieval with Key Stretching to prevent brute-force attacks.
            //-------------------------------------------------------------------------------------
            String parts = Password.createHash(password);

            // Reset User object's password
            user.setPassword(parts);

            // Update the database
            getUserFacade().edit(user);

            // Initialize the instance variables
            username = password = confirmPassword = answerToSecurityQuestion = "";

            Methods.showMessage("Information", "Success!", "Your Password has been Reset!");

            // Redirect to show the index (home) page
            return "/index?faces-redirect=true";

        } catch (EJBException | Password.CannotPerformOperationException ex) {
            Methods.showMessage("Fatal Error", "Something went wrong while resetting your password!",
                    "See: " + ex.getMessage());
        }
        return "";
    }

}
