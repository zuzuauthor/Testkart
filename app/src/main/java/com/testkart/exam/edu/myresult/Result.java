
package com.testkart.exam.edu.myresult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Result implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("exam_id")
    @Expose
    private String examId;
    @SerializedName("student_id")
    @Expose
    private String studentId;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("resume_time")
    @Expose
    private Object resumeTime;
    @SerializedName("total_question")
    @Expose
    private String totalQuestion;
    @SerializedName("total_attempt")
    @Expose
    private String totalAttempt;
    @SerializedName("total_answered")
    @Expose
    private String totalAnswered;
    @SerializedName("total_marks")
    @Expose
    private String totalMarks;
    @SerializedName("obtained_marks")
    @Expose
    private String obtainedMarks;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("percent")
    @Expose
    private String percent;
    @SerializedName("finalized_time")
    @Expose
    private String finalizedTime;
    @SerializedName("user_id")
    @Expose
    private String userId;
    private final static long serialVersionUID = 2769551337976327837L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Object getResumeTime() {
        return resumeTime;
    }

    public void setResumeTime(Object resumeTime) {
        this.resumeTime = resumeTime;
    }

    public String getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(String totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public String getTotalAttempt() {
        return totalAttempt;
    }

    public void setTotalAttempt(String totalAttempt) {
        this.totalAttempt = totalAttempt;
    }

    public String getTotalAnswered() {
        return totalAnswered;
    }

    public void setTotalAnswered(String totalAnswered) {
        this.totalAnswered = totalAnswered;
    }

    public String getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(String totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getObtainedMarks() {
        return obtainedMarks;
    }

    public void setObtainedMarks(String obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getFinalizedTime() {
        return finalizedTime;
    }

    public void setFinalizedTime(String finalizedTime) {
        this.finalizedTime = finalizedTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
