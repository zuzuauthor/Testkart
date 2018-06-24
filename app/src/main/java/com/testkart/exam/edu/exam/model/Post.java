
package com.testkart.exam.edu.exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("Exam")
    @Expose
    private Exam_ exam;

    public Exam_ getExam() {
        return exam;
    }

    public void setExam(Exam_ exam) {
        this.exam = exam;
    }

}
