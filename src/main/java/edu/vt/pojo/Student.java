package edu.vt.pojo;

import java.io.Serializable;

public class Student implements Serializable {
    private Integer id;
    private String name;
    private Integer score;

    public Student(Integer id, String name, Integer score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Integer getScore() { return score; }

    public void setScore(Integer score) { this.score = score; }
}
