/*
 * Created by Calvin Huang on 2021.3.5
 * Copyright © 2021 Calvin Huang. All rights reserved.
 */
package edu.vt.controllers;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named(value = "sliderController")
@RequestScoped

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
