
package com.testkart.exam.edu.exam.ibps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExamStat___ implements Serializable
{

    @SerializedName("ques_no")
    @Expose
    private String quesNo;
    @SerializedName("opened")
    @Expose
    private String opened;
    @SerializedName("answered")
    @Expose
    private String answered;
    @SerializedName("review")
    @Expose
    private String review;
    private final static long serialVersionUID = -7073495363090782115L;

    public String getQuesNo() {
        return quesNo;
    }

    public void setQuesNo(String quesNo) {
        this.quesNo = quesNo;
    }

    public String getOpened() {
        return opened;
    }

    public void setOpened(String opened) {
        this.opened = opened;
    }

    public String getAnswered() {
        return answered;
    }

    public void setAnswered(String answered) {
        this.answered = answered;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

}
