package com.testkart.exam.edu.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by testkart on 12/4/17.       CRMCCCBB70388
 */

public class UserInfo {

    @SerializedName("id")
    private String studentId;

    @SerializedName("name")
    private String studentName;

    @SerializedName("email")
    private String studentEmail;

    @SerializedName("password")
    private String password;

    @SerializedName("profile_image")
    private String profileImage;

    @SerializedName("account_balance")
    private String accountBlance;

    @SerializedName("user_status")
    private String userStatus;

    //comma seprated string
    @SerializedName("course_tags")
    private String courseTags;

    @SerializedName("course_tags")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCourseTags() {
        return courseTags;
    }

    public void setCourseTags(String courseTags) {
        this.courseTags = courseTags;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String isStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getAccountBlance() {
        return accountBlance;
    }

    public void setAccountBlance(String accountBlance) {
        this.accountBlance = accountBlance;
    }
}
