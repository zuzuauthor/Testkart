package com.testkart.exam.packages.model;

import java.io.Serializable;

/**
 * Created by testkart on 12/6/17.
 */

public class DataOrderSummary implements Serializable {

    private String packageId;
    private String packageName;
    private String packageDescription;
    private String packageAmount;
    private String packageSHowAmount;
    private String packageExpiryDays;
    private String packageStatus;
    private String packageCreated;
    private String packageModify;
    private String packageImage;
    private String packageExamIds;
    private String packageExamName;
    private String packageOrdering;
    private String packageCurrencyCode;
    private boolean isItemAddedToCart;


    public String getPackageOrdering() {
        return packageOrdering;
    }

    public void setPackageOrdering(String packageOrdering) {
        this.packageOrdering = packageOrdering;
    }

    public boolean isItemAddedToCart() {
        return isItemAddedToCart;
    }

    public void setItemAddedToCart(boolean itemAddedToCart) {
        isItemAddedToCart = itemAddedToCart;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }

    public String getPackageAmount() {
        return packageAmount;
    }

    public void setPackageAmount(String packageAmount) {
        this.packageAmount = packageAmount;
    }

    public String getPackageSHowAmount() {
        return packageSHowAmount;
    }

    public void setPackageSHowAmount(String packageSHowAmount) {
        this.packageSHowAmount = packageSHowAmount;
    }

    public String getPackageExpiryDays() {
        return packageExpiryDays;
    }

    public void setPackageExpiryDays(String packageExpiryDays) {
        this.packageExpiryDays = packageExpiryDays;
    }

    public String getPackageStatus() {
        return packageStatus;
    }

    public void setPackageStatus(String packageStatus) {
        this.packageStatus = packageStatus;
    }

    public String getPackageCreated() {
        return packageCreated;
    }

    public void setPackageCreated(String packageCreated) {
        this.packageCreated = packageCreated;
    }

    public String getPackageModify() {
        return packageModify;
    }

    public void setPackageModify(String packageModify) {
        this.packageModify = packageModify;
    }

    public String getPackageImage() {
        return packageImage;
    }

    public void setPackageImage(String packageImage) {
        this.packageImage = packageImage;
    }

    public String getPackageExamIds() {
        return packageExamIds;
    }

    public void setPackageExamIds(String packageExamIds) {
        this.packageExamIds = packageExamIds;
    }

    public String getPackageExamName() {
        return packageExamName;
    }

    public void setPackageExamName(String packageExamName) {
        this.packageExamName = packageExamName;
    }

    public String getPackageCurrencyCode() {
        return packageCurrencyCode;
    }

    public void setPackageCurrencyCode(String packageCurrencyCode) {
        this.packageCurrencyCode = packageCurrencyCode;
    }
}
