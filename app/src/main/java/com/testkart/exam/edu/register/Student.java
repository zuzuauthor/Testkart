
package com.testkart.exam.edu.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Student implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("guardian_phone")
    @Expose
    private String guardianPhone;
    @SerializedName("enroll")
    @Expose
    private String enroll;
    @SerializedName("photo")
    @Expose
    private Object photo;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("reg_code")
    @Expose
    private String regCode;
    @SerializedName("reg_status")
    @Expose
    private String regStatus;
    @SerializedName("expiry_days")
    @Expose
    private Object expiryDays;
    @SerializedName("renewal_date")
    @Expose
    private String renewalDate;
    @SerializedName("presetcode")
    @Expose
    private Object presetcode;
    @SerializedName("public_key")
    @Expose
    private Object publicKey;
    @SerializedName("private_key")
    @Expose
    private Object privateKey;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("last_login")
    @Expose
    private Object lastLogin;

    @SerializedName("student_group")
    @Expose
    private String studentGroup;

    private final static long serialVersionUID = -3390048824260734888L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGuardianPhone() {
        return guardianPhone;
    }

    public void setGuardianPhone(String guardianPhone) {
        this.guardianPhone = guardianPhone;
    }

    public String getEnroll() {
        return enroll;
    }

    public void setEnroll(String enroll) {
        this.enroll = enroll;
    }

    public Object getPhoto() {
        return photo;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegCode() {
        return regCode;
    }

    public void setRegCode(String regCode) {
        this.regCode = regCode;
    }

    public String getRegStatus() {
        return regStatus;
    }

    public void setRegStatus(String regStatus) {
        this.regStatus = regStatus;
    }

    public Object getExpiryDays() {
        return expiryDays;
    }

    public void setExpiryDays(Object expiryDays) {
        this.expiryDays = expiryDays;
    }

    public String getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(String renewalDate) {
        this.renewalDate = renewalDate;
    }

    public Object getPresetcode() {
        return presetcode;
    }

    public void setPresetcode(Object presetcode) {
        this.presetcode = presetcode;
    }

    public Object getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(Object publicKey) {
        this.publicKey = publicKey;
    }

    public Object getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(Object privateKey) {
        this.privateKey = privateKey;
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

    public Object getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Object lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getStudentGroup() {
        return studentGroup;
    }

    public void setStudentGroup(String studentGroup) {
        this.studentGroup = studentGroup;
    }
}
