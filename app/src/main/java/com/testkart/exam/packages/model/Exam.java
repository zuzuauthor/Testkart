
package com.testkart.exam.packages.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Exam implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("instruction")
    @Expose
    private String instruction;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("passing_percent")
    @Expose
    private String passingPercent;
    @SerializedName("negative_marking")
    @Expose
    private String negativeMarking;
    @SerializedName("attempt_count")
    @Expose
    private String attemptCount;
    @SerializedName("declare_result")
    @Expose
    private String declareResult;
    @SerializedName("finish_result")
    @Expose
    private String finishResult;
    @SerializedName("ques_random")
    @Expose
    private String quesRandom;
    @SerializedName("paid_exam")
    @Expose
    private String paidExam;
    @SerializedName("browser_tolrance")
    @Expose
    private String browserTolrance;
    @SerializedName("instant_result")
    @Expose
    private String instantResult;
    @SerializedName("option_shuffle")
    @Expose
    private String optionShuffle;
    @SerializedName("amount")
    @Expose
    private Object amount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("multi_language")
    @Expose
    private String multiLanguage;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("expiry")
    @Expose
    private String expiry;
    @SerializedName("finalized_time")
    @Expose
    private Object finalizedTime;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("ExamsPackage")
    @Expose
    private ExamsPackage examsPackage;
    private final static long serialVersionUID = -631193972428312361L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
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

    public String getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(String attemptCount) {
        this.attemptCount = attemptCount;
    }

    public String getDeclareResult() {
        return declareResult;
    }

    public void setDeclareResult(String declareResult) {
        this.declareResult = declareResult;
    }

    public String getFinishResult() {
        return finishResult;
    }

    public void setFinishResult(String finishResult) {
        this.finishResult = finishResult;
    }

    public String getQuesRandom() {
        return quesRandom;
    }

    public void setQuesRandom(String quesRandom) {
        this.quesRandom = quesRandom;
    }

    public String getPaidExam() {
        return paidExam;
    }

    public void setPaidExam(String paidExam) {
        this.paidExam = paidExam;
    }

    public String getBrowserTolrance() {
        return browserTolrance;
    }

    public void setBrowserTolrance(String browserTolrance) {
        this.browserTolrance = browserTolrance;
    }

    public String getInstantResult() {
        return instantResult;
    }

    public void setInstantResult(String instantResult) {
        this.instantResult = instantResult;
    }

    public String getOptionShuffle() {
        return optionShuffle;
    }

    public void setOptionShuffle(String optionShuffle) {
        this.optionShuffle = optionShuffle;
    }

    public Object getAmount() {
        return amount;
    }

    public void setAmount(Object amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMultiLanguage() {
        return multiLanguage;
    }

    public void setMultiLanguage(String multiLanguage) {
        this.multiLanguage = multiLanguage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public Object getFinalizedTime() {
        return finalizedTime;
    }

    public void setFinalizedTime(Object finalizedTime) {
        this.finalizedTime = finalizedTime;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public ExamsPackage getExamsPackage() {
        return examsPackage;
    }

    public void setExamsPackage(ExamsPackage examsPackage) {
        this.examsPackage = examsPackage;
    }

}
