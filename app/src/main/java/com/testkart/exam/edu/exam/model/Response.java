
package com.testkart.exam.edu.exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Response implements Serializable
{

    private final static long serialVersionUID = -298087096385779061L;

    @SerializedName("question_id")
    @Expose
    private Integer questionId;
    @SerializedName("question_no")
    @Expose
    private Integer questionNo;
    @SerializedName("question_type")
    @Expose
    private String questionType;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("question_status")
    @Expose
    private Integer questionStatus;
    @SerializedName("attempt_time")
    @Expose
    private String attemptTime;
    @SerializedName("opened")
    @Expose
    private Integer opened;
    @SerializedName("answered")
    @Expose
    private Integer answered;
    @SerializedName("review")
    @Expose
    private Integer review;
    @SerializedName("time_taken")
    @Expose
    private Integer timeTaken;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(Integer questionNo) {
        this.questionNo = questionNo;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Integer getQuestionStatus() {
        return questionStatus;
    }

    public void setQuestionStatus(Integer questionStatus) {
        this.questionStatus = questionStatus;
    }

    public String getAttemptTime() {
        return attemptTime;
    }

    public void setAttemptTime(String attemptTime) {
        this.attemptTime = attemptTime;
    }

    public Integer getOpened() {
        return opened;
    }

    public void setOpened(Integer opened) {
        this.opened = opened;
    }

    public Integer getAnswered() {
        return answered;
    }

    public void setAnswered(Integer answered) {
        this.answered = answered;
    }

    public Integer getReview() {
        return review;
    }

    public void setReview(Integer review) {
        this.review = review;
    }

    public Integer getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Integer timeTaken) {
        this.timeTaken = timeTaken;
    }

    /*@SerializedName("question_id")
    @Expose
    private String questionId;
    @SerializedName("question_no")
    @Expose
    private String questionNo;
    @SerializedName("question_type")
    @Expose
    private String questionType;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("question_status")
    @Expose
    private String questionStatus;
    @SerializedName("attempt_time")
    @Expose
    private String attemptTime;
    @SerializedName("opened")
    @Expose
    private String opened;
    @SerializedName("answered")
    @Expose
    private String answered;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("time_taken")
    @Expose
    private String timeTaken;


    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getQuestionStatus() {
        return questionStatus;
    }

    public void setQuestionStatus(String questionStatus) {
        this.questionStatus = questionStatus;
    }

    public String getAttemptTime() {
        return attemptTime;
    }

    public void setAttemptTime(String attemptTime) {
        this.attemptTime = attemptTime;
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

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }*/

}
