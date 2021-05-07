/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
package edu.vt.managers;

import edu.vt.EntityBeans.User;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.globals.Methods;
import edu.vt.globals.Password;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named(value = "loginManager")
@SessionScoped

public class LoginManager implements Serializable {

    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private String username;
    private String password;

    /*
    The instance variable 'userFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the WildFly AS) to inject (store) the object reference
    of the UserFacade object, after it is instantiated at runtime, into the instance variable 'userFacade'.
     */
    @EJB
    private UserFacade userFacade;

    /*
    ==================
    Constructor Method
    ==================
     */
    public LoginManager() {
    }

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

    public UserFacade getUserFacade() {
        return userFacade;
    }

    /*
    ================
    Instance Methods
    ================

    *****************************************************
    Sign in the User if the Entered Username and Password
    are Valid and Redirect to Show the Profile Page
    *****************************************************
     */
    public String loginUser() {

        // Since we will redirect to show the Profile page, invoke preserveMessages()
        Methods.preserveMessages();

        String enteredUsername = getUsername();

        // Obtain the object reference of the User object from the entered username
        User user = getUserFacade().findByUsername(enteredUsername);

        if (user == null) {
            Methods.showMessage("Fatal Error", "Unknown Username!",
                    "Entered username " + enteredUsername + " does not exist!");
            return "";

        } else {
            String actualUsername = user.getUsername();

            String actualPassword = user.getPassword();
            String enteredPassword = getPassword();

            if (!actualUsername.equals(enteredUsername)) {
                Methods.showMessage("Fatal Error", "Invalid Username!", "Entered Username is Unknown!");
                return "";
            }

            //------------------------------------------------------------------------------------
            // Obtain the user's password String containing the following parts from the database
            //       "algorithmName":"PBKDF2_ITERATIONS":"hashSize":"salt":"hash",
            // extract the actual password from the parts, and compare it with the password String
            // entered by the user by using Key Stretching to prevent brute-force attacks.
            //------------------------------------------------------------------------------------            
            try {
                if (Password.verifyPassword(enteredPassword, actualPassword)) {
                    // entered password = user's actual password
                } else {
                    Methods.showMessage("Fatal Error", "Invalid Password!", "Please Enter a Valid Password!");
                    return "";
                }
            } catch (Password.CannotPerformOperationException | Password.InvalidHashException ex) {

                Methods.showMessage("Fatal Error", "Password Manager was unable to perform its operation!",
                        "See: " + ex.getMessage());
                return "";
            }

            // Initialize the session map with user properties of interest
            initializeSessionMap(user);

            // Redirect to show the Profile page
            return "/userAccount/Profile?faces-redirect=true";
        }
    }

    /*
    ******************************************************************
    Initialize the Session Map to Hold Session Attributes of Interests 
    ******************************************************************
     */
    public void initializeSessionMap(User user) {
        // Store the object reference of the signed-in user
        Methods.sessionMap().put("user", user);

        // Store the First Name of the signed-in user
        Methods.sessionMap().put("first_name", user.getFirstName());

        // Store the Last Name of the signed-in user
        Methods.sessionMap().put("last_name", user.getLastName());

        // Store the Username of the signed-in user
        Methods.sessionMap().put("username", username);

        // Store signed-in user's Primary Key in the database
        Methods.sessionMap().put("user_id", user.getId());
    }

}
