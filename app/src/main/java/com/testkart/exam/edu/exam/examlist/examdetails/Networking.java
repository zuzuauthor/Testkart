
package com.testkart.exam.edu.exam.examlist.examdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Networking implements Serializable
{

    @SerializedName("total_question")
    @Expose
    private Integer totalQuestion;
    @SerializedName("total_attempt_question")
    @Expose
    private Integer totalAttemptQuestion;
    private final static long serialVersionUID = -2014993246490202387L;

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
