
package com.testkart.exam.edu.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OTPModel implements Serializable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("accountStatus")
    @Expose
    private String accountStatus;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("public_key")
    @Expose
    private String publicKey;
    @SerializedName("private_key")
    @Expose
    private String privateKey;
    @SerializedName("studentPhoto")
    @Expose
    private Object studentPhoto;
    @SerializedName("sysSetting")
    @Expose
    private SysSetting sysSetting;
    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("currencyCode")
    @Expose
    private String currencyCode;

    private final static long serialVersionUID = -6827820445066629085L;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public Object getStudentPhoto() {
        return studentPhoto;
    }

    public void setStudentPhoto(Object studentPhoto) {
        this.studentPhoto = studentPhoto;
    }

    public SysSetting getSysSetting() {
        return sysSetting;
    }

    public void setSysSetting(SysSetting sysSetting) {
        this.sysSetting = sysSetting;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
