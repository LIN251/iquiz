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
The "usernameValidator" attribute is the validator-id used in the JSF facelets page CreateAccount.xhtml within

    <f:validator validatorId="usernameValidator" /> 

to invoke the "validate" method of the annotated class given below.                           
*/
@FacesValidator("usernameValidator")

public class UsernameValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        
        // Type cast the inputted "value" to String type
        String username = (String) value;

        if (username == null || username.isEmpty()) {
            // Do not take any action. 
            // The required="true" in the XHTML file will catch this and produce an error message.
            return;
        }
        
        /* REGular EXpression (regex) for validating the username

            ^                   start of regex
            [_A-Za-z0-9.@-]     can contain underscore, A to Z, a to z, 0 to 9, period, at sign, and hyphen.
                                Hyphen must be specified at the end since it means range between two characters.
                                We allow @ and . so that a simple email address can be used as a username.
            {6,32}              can be at least 6 characters and maximum 32 characters long 
            $                   end of regex
        */
        
        // REGular EXpression (regex) to validate the username entered
        String regex = "^[_A-Za-z0-9.@-]{6,32}$";
        
        if (!username.matches(regex)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_WARN, 
                    "Username Failed!", "The username must be minimum 6 and maximum 32 "
                    + "characters long and can contain underscore, uppercase letters, "
                    + "lowercase letters, digits 0 to 9, period, at sign @, and hyphen."));
        } 
    } 
    
}
