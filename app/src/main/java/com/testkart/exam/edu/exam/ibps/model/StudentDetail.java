
package com.testkart.exam.edu.exam.ibps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentDetail implements Serializable
{

    @SerializedName("Student")
    @Expose
    private Student student;
    private final static long serialVersionUID = -6494017232743819824L;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

}
