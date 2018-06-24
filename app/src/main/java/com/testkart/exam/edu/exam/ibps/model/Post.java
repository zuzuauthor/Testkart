
package com.testkart.exam.edu.exam.ibps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post implements Serializable
{

    @SerializedName("Exam")
    @Expose
    private Exam_ exam;
    private final static long serialVersionUID = -4346593525753015812L;

    public Exam_ getExam() {
        return exam;
    }

    public void setExam(Exam_ exam) {
        this.exam = exam;
    }

}
