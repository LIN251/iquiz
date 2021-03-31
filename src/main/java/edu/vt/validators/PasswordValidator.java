/*
 * Created by Osman Balci on 2021.2.6
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/*
The @FacesValidator annotation on a class automatically registers the class with the runtime as a Validator. 
The "passwordValidator" attribute is the validator-id used in the JSF facelets page CreateAccount.xhtml within

    <f:validator validatorId="passwordValidator" /> 

to invoke the "validate" method of the annotated class given below.                           
*/
@FacesValidator("passwordValidator")

public class PasswordValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        // Type cast the inputted "value" to String type
        String enteredPassword = (String) value;

        if (enteredPassword == null || enteredPassword.isEmpty()) {
            // The required="true" in the XHTML file will catch this and produce an error message.
            return;
        }

        /*
        A whitespace is a character such as space " ", newline "\n", tab "\t", return "\r"
        First, check to see if the entered password contains a whitespace
         */
        int pwdLength = enteredPassword.length();
        for (int i = 0; i < pwdLength; i++) {
            if (Character.isWhitespace(enteredPassword.charAt(i))) {

                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL, 
                        "Password cannot contain a whitespace!", "Please enter a valid password!"));
            }
        }

        /* REGular EXpression (regex) for validating the strength of enteredPassword
        
            ^                   start of regex
            (?=.{8,32})         minimum 8 and maximum 32 characters long  
            (?=.*[!@#$%^&*()])  contain at least one of the special characters
                                above the numbers 1, 2, 3, ..., 9, 0 on the keyboard.
            (?=.*[A-Z])         contain at least one uppercase letter
            (?=.*[a-z])         contain at least one lowercase letter
            (?=.*[0-9])         contain at least one digit from 0 to 9.
            $                   end of regex
         */
        // REGular EXpression (regex) to validate the strength of enteredPassword
        String regex = "^(?=.{8,32})(?=.*[!@#$%^&*()])(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).*$";

        if (!enteredPassword.matches(regex)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_WARN, 
                    "Password Failed!", "The password must be minimum 8 and maximum 32 "
                    + "characters long, contain at least one special character above the numbers on the keyboard, "
                    + "contain at least one uppercase letter, "
                    + "contain at least one lowercase letter, "
                    + "and contain at least one digit 0 to 9."));
        }
    }

}
