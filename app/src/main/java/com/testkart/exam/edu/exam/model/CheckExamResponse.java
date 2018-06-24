package com.testkart.exam.edu.exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by testkart on 31/5/17.
 */

public class CheckExamResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("testId")
    @Expose
    private String testId;
    @SerializedName("remExamName")
    @Expose
    private String remExamName;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getRemExamName() {
        return remExamName;
    }

    public void setRemExamName(String remExamName) {
        this.remExamName = remExamName;
    }

}