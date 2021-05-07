/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
package edu.vt.controllers;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/*
---------------------------------------------------------------------------
The @Named (javax.inject.Named) annotation indicates that the objects
instantiated from this class will be managed by the Contexts and Dependency
Injection (CDI) container. The name "sliderController" is used within
Expression Language (EL) expressions in JSF (XHTML) facelets pages to
access the properties and invoke methods of this class.
---------------------------------------------------------------------------
 */
@Named(value = "sliderController")
/*
The @SessionScoped annotation preserves the values of the SliderController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@RequestScoped

/*
--------------------------------------------------------------------------
Marking the SliderController class as "implements Serializable" implies that
instances of the class can be automatically serialized and deserialized.

Serialization is the process of converting a class instance (object)
from memory into a suitable format for storage in a file or memory buffer,
or for transmission across a network connection link.

Deserialization is the process of recreating a class instance (object)
in memory from the format under which it was stored.
--------------------------------------------------------------------------
 */
public class SliderController {

    // Each String object in the List contains the image filename, e.g., photo1.png
    private List<String> sliderImages;

    /*
    The PostConstruct annotation is used on a method that needs to be executed after
    dependency injection is done to perform any initialization. The initialization
    method init() is the first method invoked before this class is put into service.
    */
    @PostConstruct
    public void init() {

        sliderImages = new ArrayList<>();

        for (int i = 1; i <= 9; i++) {
            sliderImages.add("photo" + i + ".png");
        }
    }

    /*
    =============
    Getter Method
    =============
     */
    public List<String> getSliderImages() {
        return sliderImages;
    }

    /*
    ===============
    Instance Method
    ===============
     */
    public String description(String image) {

        String imageDescription = "";

        switch (image) {
            case "photo1.png":
                imageDescription = "Sign up iQuiz";
                break;
            case "photo2.png":
                imageDescription = "Login iQuiz";
                break;
            case "photo3.png":
                imageDescription = "Quizzes bank";
                break;
            case "photo4.png":
                imageDescription = "Create quiz on iQuiz";
                break;
            case "photo5.png":
                imageDescription = "Access quiz by QR code";
                break;
            case "photo6.png":
                imageDescription = "Access quiz by Access code";
                break;
            case "photo7.png":
                imageDescription = "Quiz view";
                break;
            case "photo8.png":
                imageDescription = "Publish your own quizzes";
                break;
            case "photo9.png":
                imageDescription = "Show results";
                break;
        }

        return imageDescription;
    }
}
