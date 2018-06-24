
package com.testkart.exam.payment.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Package implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("show_amount")
    @Expose
    private String showAmount;

    @SerializedName("payments_for")
    @Expose
    private String paymentsFor;

    @SerializedName("photo")
    @Expose
    private Object photo;
    @SerializedName("expiry_days")
    @Expose
    private String expiryDays;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("PackagesPayment")
    @Expose
    private PackagesPayment packagesPayment;
    @SerializedName("Exam")
    @Expose
    private List<Exam> exam = null;
    private final static long serialVersionUID = -6640845033042416236L;


    public String getPaymentsFor() {
        return paymentsFor;
    }

    public void setPaymentsFor(String paymentsFor) {
        this.paymentsFor = paymentsFor;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getShowAmount() {
        return showAmount;
    }

    public void setShowAmount(String showAmount) {
        this.showAmount = showAmount;
    }

    public Object getPhoto() {
        return photo;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public String getExpiryDays() {
        return expiryDays;
    }

    public void setExpiryDays(String expiryDays) {
        this.expiryDays = expiryDays;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public PackagesPayment getPackagesPayment() {
        return packagesPayment;
    }

    public void setPackagesPayment(PackagesPayment packagesPayment) {
        this.packagesPayment = packagesPayment;
    }

    public List<Exam> getExam() {
        return exam;
    }

    public void setExam(List<Exam> exam) {
        this.exam = exam;
    }

}
