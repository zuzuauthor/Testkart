
package com.testkart.exam.edu.exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class ExamResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;

    @SerializedName("examExpire")
    @Expose
    private Boolean examExpire;

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("userExamQuestion")
    @Expose
    private List<UserExamQuestion> userExamQuestion = null;

    /*@SerializedName("userSectionQuestion")
    @Expose
    private UserSectionQuestion userSectionQuestion;*/


    @SerializedName("userSectionQuestion")
    @Expose
    private Map<String, List<UserSectionQuestion>> userSectionQuestion;

    public Map<String, List<UserSectionQuestion>> getUserSectionQuestion() {
        return userSectionQuestion;
    }

    public void setUserSectionQuestion(Map<String, List<UserSectionQuestion>> userSectionQuestion) {
        this.userSectionQuestion = userSectionQuestion;
    }


    @SerializedName("languageArr")
    @Expose
    private Map<String, String> languageArr;

    public Map<String, String> getLanguageArr() {
        return languageArr;
    }

    public void setLanguageArr(Map<String, String> languageArr) {
        this.languageArr = languageArr;
    }

    @SerializedName("currSubjectName")
    @Expose
    private String currSubjectName;
    @SerializedName("post")
    @Expose
    private Post post;
    @SerializedName("examResult")
    @Expose
    private ExamResult examResult;
    @SerializedName("totalQuestion")
    @Expose
    private Integer totalQuestion;
    @SerializedName("examResultId")
    @Expose
    private String examResultId;
    @SerializedName("siteDomain")
    @Expose
    private String siteDomain;
    @SerializedName("studentDetail")
    @Expose
    private StudentDetail studentDetail;
    @SerializedName("studentPhoto")
    @Expose
    private Object studentPhoto;

    public boolean isExamExpire() {
        return examExpire;
    }

    public void setExamExpire(boolean examExpire) {
        this.examExpire = examExpire;
    }

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

    public List<UserExamQuestion> getUserExamQuestion() {
        return userExamQuestion;
    }

    public void setUserExamQuestion(List<UserExamQuestion> userExamQuestion) {
        this.userExamQuestion = userExamQuestion;
    }

    /*public UserSectionQuestion getUserSectionQuestion() {
        return userSectionQuestion;
    }

    public void setUserSectionQuestion(UserSectionQuestion userSectionQuestion) {
        this.userSectionQuestion = userSectionQuestion;
    }*/

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

    public String getSiteDomain() {
        return siteDomain;
    }

    public void setSiteDomain(String siteDomain) {
        this.siteDomain = siteDomain;
    }

    public StudentDetail getStudentDetail() {
        return studentDetail;
    }

    public void setStudentDetail(StudentDetail studentDetail) {
        this.studentDetail = studentDetail;
    }

    public Object getStudentPhoto() {
        return studentPhoto;
    }

    public void setStudentPhoto(Object studentPhoto) {
        this.studentPhoto = studentPhoto;
    }

}
