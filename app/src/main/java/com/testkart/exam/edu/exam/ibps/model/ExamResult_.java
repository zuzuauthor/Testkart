
package com.testkart.exam.edu.exam.ibps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExamResult_ implements Serializable
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
    private Object endTime;
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
    private Object totalAnswered;
    @SerializedName("total_marks")
    @Expose
    private String totalMarks;
    @SerializedName("obtained_marks")
    @Expose
    private Object obtainedMarks;
    @SerializedName("result")
    @Expose
    private Object result;
    @SerializedName("percent")
    @Expose
    private Object percent;
    @SerializedName("finalized_time")
    @Expose
    private Object finalizedTime;
    @SerializedName("user_id")
    @Expose
    private Object userId;
    private final static long serialVersionUID = 5378309805848797747L;

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

    public Object getEndTime() {
        return endTime;
    }

    public void setEndTime(Object endTime) {
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

    public Object getTotalAnswered() {
        return totalAnswered;
    }

    public void setTotalAnswered(Object totalAnswered) {
        this.totalAnswered = totalAnswered;
    }

    public String getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(String totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Object getObtainedMarks() {
        return obtainedMarks;
    }

    public void setObtainedMarks(Object obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getPercent() {
        return percent;
    }

    public void setPercent(Object percent) {
        this.percent = percent;
    }

    public Object getFinalizedTime() {
        return finalizedTime;
    }

    public void setFinalizedTime(Object finalizedTime) {
        this.finalizedTime = finalizedTime;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

}
