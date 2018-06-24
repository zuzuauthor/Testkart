
package com.testkart.exam.edu.myresult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Response implements Serializable
{

    @SerializedName("Result")
    @Expose
    private Result result;
    @SerializedName("Exam")
    @Expose
    private Exam exam;
    private final static long serialVersionUID = 2425656119763170377L;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

}
