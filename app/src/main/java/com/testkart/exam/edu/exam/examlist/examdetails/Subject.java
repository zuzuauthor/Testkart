package com.testkart.exam.edu.exam.examlist.examdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by testkart on 29/5/17.
 */

public class Subject implements Serializable
{

    @SerializedName("total_question")
    @Expose
    private Integer totalQuestion;
    @SerializedName("total_attempt_question")
    @Expose
    private Integer totalAttemptQuestion;

    private String subject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    private final static long serialVersionUID = 7358559420972165881L;

    public Integer getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(Integer totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public Integer getTotalAttemptQuestion() {
        return totalAttemptQuestion;
    }

    public void setTotalAttemptQuestion(Integer totalAttemptQuestion) {
        this.totalAttemptQuestion = totalAttemptQuestion;
    }

}
