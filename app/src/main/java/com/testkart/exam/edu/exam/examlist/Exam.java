
package com.testkart.exam.edu.exam.examlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Exam implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("paid_exam")
    @Expose
    private String paidExam;
    @SerializedName("amount")
    @Expose
    private Object amount;
    @SerializedName("attempt_count")
    @Expose
    private String attemptCount;
    @SerializedName("expiry")
    @Expose
    private Object expiry;
    @SerializedName("attempt")
    @Expose
    private String attempt;
    @SerializedName("attempt_order")
    @Expose
    private String attemptOrder;

    @SerializedName("fexpiry_date")
    @Expose
    private String fexpiry_date;

    private boolean canAttempt;

    public boolean isCanAttempt() {
        return canAttempt;
    }

    public void setCanAttempt(boolean canAttempt) {
        this.canAttempt = canAttempt;
    }

    public String getFexpiry_date() {
        return fexpiry_date;
    }

    public void setFexpiry_date(String fexpiry_date) {
        this.fexpiry_date = fexpiry_date;
    }

    private final static long serialVersionUID = -3700163818762174294L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPaidExam() {
        return paidExam;
    }

    public void setPaidExam(String paidExam) {
        this.paidExam = paidExam;
    }

    public Object getAmount() {
        return amount;
    }

    public void setAmount(Object amount) {
        this.amount = amount;
    }

    public String getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(String attemptCount) {
        this.attemptCount = attemptCount;
    }

    public Object getExpiry() {
        return expiry;
    }

    public void setExpiry(Object expiry) {
        this.expiry = expiry;
    }

    public String getAttempt() {
        return attempt;
    }

    public void setAttempt(String attempt) {
        this.attempt = attempt;
    }

    public String getAttemptOrder() {
        return attemptOrder;
    }

    public void setAttemptOrder(String attemptOrder) {
        this.attemptOrder = attemptOrder;
    }

}
