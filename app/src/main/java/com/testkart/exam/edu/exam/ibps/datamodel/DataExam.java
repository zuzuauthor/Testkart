package com.testkart.exam.edu.exam.ibps.datamodel;

import java.io.Serializable;

/**
 * Created by testkart on 1/7/17.
 */

public class DataExam implements Serializable{

    private String examId;
    private String examName;
    private String examInstruction;
    private String duration;
    private String startDate;
    private String endDate;
    private String passingPercent;
    private String negativeMarking;
    private String paidExam;
    private String amount;
    private String examStartTime;
    private String examEndTime;
    private String examFinilize;
    private String examDurationContinue;
    private String multiLanguage;

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamInstruction() {
        return examInstruction;
    }

    public void setExamInstruction(String examInstruction) {
        this.examInstruction = examInstruction;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPassingPercent() {
        return passingPercent;
    }

    public void setPassingPercent(String passingPercent) {
        this.passingPercent = passingPercent;
    }

    public String getNegativeMarking() {
        return negativeMarking;
    }

    public void setNegativeMarking(String negativeMarking) {
        this.negativeMarking = negativeMarking;
    }

    public String getPaidExam() {
        return paidExam;
    }

    public void setPaidExam(String paidExam) {
        this.paidExam = paidExam;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExamStartTime() {
        return examStartTime;
    }

    public void setExamStartTime(String examStartTime) {
        this.examStartTime = examStartTime;
    }

    public String getExamEndTime() {
        return examEndTime;
    }

    public void setExamEndTime(String examEndTime) {
        this.examEndTime = examEndTime;
    }

    public String getExamFinilize() {
        return examFinilize;
    }

    public void setExamFinilize(String examFinilize) {
        this.examFinilize = examFinilize;
    }

    public String getExamDurationContinue() {
        return examDurationContinue;
    }

    public void setExamDurationContinue(String examDurationContinue) {
        this.examDurationContinue = examDurationContinue;
    }

    public String getMultiLanguage() {
        return multiLanguage;
    }

    public void setMultiLanguage(String multiLanguage) {
        this.multiLanguage = multiLanguage;
    }
}
