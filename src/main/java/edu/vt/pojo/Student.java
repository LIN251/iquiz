/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
package edu.vt.pojo;

import java.io.Serializable;

public class Student implements Serializable {
    /*
    ========================================================
    Instance variables representing the attributes
    ========================================================
    */
    private Integer id;
    private String name;
    private Integer score;

    /*
    ===============================================================
    Class constructors
    ===============================================================
    */
    public Student(Integer id, String name, Integer score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    /*
    ======================================================
    Getter and Setter methods
    ======================================================
     */
    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Integer getScore() { return score; }

    public void setScore(Integer score) { this.score = score; }
}
