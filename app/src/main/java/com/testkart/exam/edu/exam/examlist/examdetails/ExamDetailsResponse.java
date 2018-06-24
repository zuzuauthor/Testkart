
package com.testkart.exam.edu.exam.examlist.examdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

public class ExamDetailsResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response")
    @Expose
    private Response response;


    @SerializedName("isPaid")
    @Expose
    private Boolean isPaid;

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }


    /*@SerializedName("subjectDetail")
    @Expose
    private SubjectDetail subjectDetail;*/


    @SerializedName("subjectDetail")
    @Expose
    private Map<String, Subject> subjectDetail;



    @SerializedName("languageArr")
    @Expose
    private Map<String, String> languageArr;

    public Map<String, String> getLanguageArr() {
        return languageArr;
    }

    public void setLanguageArr(Map<String, String> languageArr) {
        this.languageArr = languageArr;
    }

    public Map<String, Subject> getSubjectDetail() {
        return subjectDetail;
    }

    public void setSubjectDetail(Map<String, Subject> subjectDetail) {
        this.subjectDetail = subjectDetail;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @SerializedName("totalMarks")
    @Expose
    private String totalMarks;
    @SerializedName("examCount")
    @Expose
    private Integer examCount;
    @SerializedName("id")
    @Expose
    private String id;
    private final static long serialVersionUID = -1437015474156093012L;

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

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    /*public SubjectDetail getSubjectDetail() {
        return subjectDetail;
    }

    public void setSubjectDetail(SubjectDetail subjectDetail) {
        this.subjectDetail = subjectDetail;
    }*/

    public String getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(String totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Integer getExamCount() {
        return examCount;
    }

    public void setExamCount(Integer examCount) {
        this.examCount = examCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
