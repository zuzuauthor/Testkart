
package com.testkart.exam.edu.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable
{

    @SerializedName("Student")
    @Expose
    private Student student;
    private final static long serialVersionUID = -484138717279971102L;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

}
