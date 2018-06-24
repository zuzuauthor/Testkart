
package com.testkart.exam.edu.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Configuration implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("organization_name")
    @Expose
    private String organizationName;
    @SerializedName("domain_name")
    @Expose
    private String domainName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("meta_title")
    @Expose
    private String metaTitle;
    @SerializedName("meta_keyword")
    @Expose
    private String metaKeyword;
    @SerializedName("meta_content")
    @Expose
    private String metaContent;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("sms_notification")
    @Expose
    private Boolean smsNotification;
    @SerializedName("email_notification")
    @Expose
    private Boolean emailNotification;
    @SerializedName("guest_login")
    @Expose
    private Boolean guestLogin;
    @SerializedName("front_end")
    @Expose
    private Boolean frontEnd;
    @SerializedName("slides")
    @Expose
    private String slides;
    @SerializedName("translate")
    @Expose
    private String translate;
    @SerializedName("paid_exam")
    @Expose
    private String paidExam;
    @SerializedName("leader_board")
    @Expose
    private Boolean leaderBoard;
    @SerializedName("math_editor")
    @Expose
    private Boolean mathEditor;
    @SerializedName("certificate")
    @Expose
    private Boolean certificate;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("email_contact")
    @Expose
    private String emailContact;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("date_format")
    @Expose
    private String dateFormat;
    @SerializedName("exam_expiry")
    @Expose
    private String examExpiry;
    @SerializedName("exam_feedback")
    @Expose
    private Boolean examFeedback;
    @SerializedName("tolrance_count")
    @Expose
    private String tolranceCount;
    @SerializedName("min_limit")
    @Expose
    private String minLimit;
    @SerializedName("max_limit")
    @Expose
    private String maxLimit;
    @SerializedName("captcha_type")
    @Expose
    private String captchaType;
    @SerializedName("dir_type")
    @Expose
    private String dirType;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("panel1")
    @Expose
    private Boolean panel1;
    @SerializedName("panel2")
    @Expose
    private Boolean panel2;
    @SerializedName("panel3")
    @Expose
    private Boolean panel3;
    @SerializedName("ads")
    @Expose
    private Boolean ads;
    @SerializedName("testimonial")
    @Expose
    private Boolean testimonial;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    private final static long serialVersionUID = 360652491522790050L;

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

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaKeyword() {
        return metaKeyword;
    }

    public void setMetaKeyword(String metaKeyword) {
        this.metaKeyword = metaKeyword;
    }

    public String getMetaContent() {
        return metaContent;
    }

    public void setMetaContent(String metaContent) {
        this.metaContent = metaContent;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean getSmsNotification() {
        return smsNotification;
    }

    public void setSmsNotification(Boolean smsNotification) {
        this.smsNotification = smsNotification;
    }

    public Boolean getEmailNotification() {
        return emailNotification;
    }

    public void setEmailNotification(Boolean emailNotification) {
        this.emailNotification = emailNotification;
    }

    public Boolean getGuestLogin() {
        return guestLogin;
    }

    public void setGuestLogin(Boolean guestLogin) {
        this.guestLogin = guestLogin;
    }

    public Boolean getFrontEnd() {
        return frontEnd;
    }

    public void setFrontEnd(Boolean frontEnd) {
        this.frontEnd = frontEnd;
    }

    public String getSlides() {
        return slides;
    }

    public void setSlides(String slides) {
        this.slides = slides;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getPaidExam() {
        return paidExam;
    }

    public void setPaidExam(String paidExam) {
        this.paidExam = paidExam;
    }

    public Boolean getLeaderBoard() {
        return leaderBoard;
    }

    public void setLeaderBoard(Boolean leaderBoard) {
        this.leaderBoard = leaderBoard;
    }

    public Boolean getMathEditor() {
        return mathEditor;
    }

    public void setMathEditor(Boolean mathEditor) {
        this.mathEditor = mathEditor;
    }

    public Boolean getCertificate() {
        return certificate;
    }

    public void setCertificate(Boolean certificate) {
        this.certificate = certificate;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmailContact() {
        return emailContact;
    }

    public void setEmailContact(String emailContact) {
        this.emailContact = emailContact;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getExamExpiry() {
        return examExpiry;
    }

    public void setExamExpiry(String examExpiry) {
        this.examExpiry = examExpiry;
    }

    public Boolean getExamFeedback() {
        return examFeedback;
    }

    public void setExamFeedback(Boolean examFeedback) {
        this.examFeedback = examFeedback;
    }

    public String getTolranceCount() {
        return tolranceCount;
    }

    public void setTolranceCount(String tolranceCount) {
        this.tolranceCount = tolranceCount;
    }

    public String getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(String minLimit) {
        this.minLimit = minLimit;
    }

    public String getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(String maxLimit) {
        this.maxLimit = maxLimit;
    }

    public String getCaptchaType() {
        return captchaType;
    }

    public void setCaptchaType(String captchaType) {
        this.captchaType = captchaType;
    }

    public String getDirType() {
        return dirType;
    }

    public void setDirType(String dirType) {
        this.dirType = dirType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getPanel1() {
        return panel1;
    }

    public void setPanel1(Boolean panel1) {
        this.panel1 = panel1;
    }

    public Boolean getPanel2() {
        return panel2;
    }

    public void setPanel2(Boolean panel2) {
        this.panel2 = panel2;
    }

    public Boolean getPanel3() {
        return panel3;
    }

    public void setPanel3(Boolean panel3) {
        this.panel3 = panel3;
    }

    public Boolean getAds() {
        return ads;
    }

    public void setAds(Boolean ads) {
        this.ads = ads;
    }

    public Boolean getTestimonial() {
        return testimonial;
    }

    public void setTestimonial(Boolean testimonial) {
        this.testimonial = testimonial;
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

}
