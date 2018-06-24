package com.testkart.exam.testkart.my_result.subject_report;


import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by elfemo on 18/8/17.
 */

public class SubjectReportResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("subject_report")
    @Expose
    private SubjectReport subjectReport;
    private final static long serialVersionUID = 8473656335454694485L;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SubjectReport getSubjectReport() {
        return subjectReport;
    }

    public void setSubjectReport(SubjectReport subjectReport) {
        this.subjectReport = subjectReport;
    }

}