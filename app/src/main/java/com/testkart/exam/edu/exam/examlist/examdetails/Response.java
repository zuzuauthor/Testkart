
package com.testkart.exam.edu.exam.examlist.examdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Response implements Serializable
{

    @SerializedName("Exam")
    @Expose
    private Exam exam;
    private final static long serialVersionUID = 6967808249223438268L;

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

}
