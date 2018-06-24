
package com.testkart.exam.edu.exam.ibps.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IbpsExamResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("examExpire")
    @Expose
    private Boolean examExpire;
    @SerializedName("userExamQuestion")
    @Expose
    private List<UserExamQuestion> userExamQuestion = null;

    @SerializedName("userSectionQuestion")
    @Expose
    private Map<String, List<ExamStat_>> userSectionQuestion;

    @SerializedName("currSubjectName")
    @Expose
    private String currSubjectName;
    @SerializedName("post")
    @Expose
    private Post post;
    @SerializedName("examResult")
    @Expose
    private ExamResult examResult;
    @SerializedName("examId")
    @Expose
    private String examId;
    @SerializedName("totalQuestion")
    @Expose
    private Integer totalQuestion;
    @SerializedName("examResultId")
    @Expose
    private String examResultId;
    @SerializedName("siteDomain")
    @Expose
    private Object siteDomain;
    @SerializedName("studentDetail")
    @Expose
    private StudentDetail studentDetail;
    @SerializedName("studentPhoto")
    @Expose
    private String studentPhoto;
    @SerializedName("languageArr")
    @Expose
    private Map<String, String> languageArr;
    private final static long serialVersionUID = -1885421325896827754L;

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

    public Boolean getExamExpire() {
        return examExpire;
    }

    public void setExamExpire(Boolean examExpire) {
        this.examExpire = examExpire;
    }

    public List<UserExamQuestion> getUserExamQuestion() {
        return userExamQuestion;
    }

    public void setUserExamQuestion(List<UserExamQuestion> userExamQuestion) {
        this.userExamQuestion = userExamQuestion;
    }

    public Map<String, List<ExamStat_>> getUserSectionQuestion() {
        return userSectionQuestion;
    }

    public void setUserSectionQuestion(Map<String, List<ExamStat_>> userSectionQuestion) {
        this.userSectionQuestion = userSectionQuestion;
    }

    public String getCurrSubjectName() {
        return currSubjectName;
    }

    public void setCurrSubjectName(String currSubjectName) {
        this.currSubjectName = currSubjectName;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public ExamResult getExamResult() {
        return examResult;
    }

    public void setExamResult(ExamResult examResult) {
        this.examResult = examResult;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public Integer getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(Integer totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public String getExamResultId() {
        return examResultId;
    }

    public void setExamResultId(String examResultId) {
        this.examResultId = examResultId;
    }

    public Object getSiteDomain() {
        return siteDomain;
    }

    public void setSiteDomain(Object siteDomain) {
        this.siteDomain = siteDomain;
    }

    public StudentDetail getStudentDetail() {
        return studentDetail;
    }

    public void setStudentDetail(StudentDetail studentDetail) {
        this.studentDetail = studentDetail;
    }

    public String getStudentPhoto() {
        return studentPhoto;
    }

    public void setStudentPhoto(String studentPhoto) {
        this.studentPhoto = studentPhoto;
    }

    public Map<String, String> getLanguageArr() {
        return languageArr;
    }

    public void setLanguageArr(Map<String, String> languageArr) {
        this.languageArr = languageArr;
    }

}
