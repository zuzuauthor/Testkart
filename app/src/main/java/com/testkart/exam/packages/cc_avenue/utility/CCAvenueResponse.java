package com.testkart.exam.packages.cc_avenue.utility;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by elfemo on 5/12/17.
 */

public class CCAvenueResponse implements Serializable
{

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("tracking_id")
    @Expose
    private String trackingId;
    @SerializedName("bank_ref_no")
    @Expose
    private String bankRefNo;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("failure_message")
    @Expose
    private String failureMessage;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("card_name")
    @Expose
    private String cardName;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("status_message")
    @Expose
    private String statusMessage;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("billing_name")
    @Expose
    private String billingName;
    @SerializedName("billing_address")
    @Expose
    private String billingAddress;
    @SerializedName("billing_city")
    @Expose
    private String billingCity;
    @SerializedName("billing_state")
    @Expose
    private String billingState;
    @SerializedName("billing_zip")
    @Expose
    private String billingZip;
    @SerializedName("billing_country")
    @Expose
    private String billingCountry;
    @SerializedName("billing_tel")
    @Expose
    private String billingTel;
    @SerializedName("billing_email")
    @Expose
    private String billingEmail;
    @SerializedName("delivery_name")
    @Expose
    private String deliveryName;
    @SerializedName("delivery_address")
    @Expose
    private String deliveryAddress;
    @SerializedName("delivery_city")
    @Expose
    private String deliveryCity;
    @SerializedName("delivery_state")
    @Expose
    private String deliveryState;
    @SerializedName("delivery_zip")
    @Expose
    private String deliveryZip;
    @SerializedName("delivery_country")
    @Expose
    private String deliveryCountry;
    @SerializedName("delivery_tel")
    @Expose
    private String deliveryTel;
    @SerializedName("merchant_param1")
    @Expose
    private String merchantParam1;
    @SerializedName("merchant_param2")
    @Expose
    private String merchantParam2;
    @SerializedName("merchant_param3")
    @Expose
    private String merchantParam3;
    @SerializedName("merchant_param4")
    @Expose
    private String merchantParam4;
    @SerializedName("merchant_param5")
    @Expose
    private String merchantParam5;
    @SerializedName("vault")
    @Expose
    private String vault;
    @SerializedName("offer_type")
    @Expose
    private String offerType;
    @SerializedName("offer_code")
    @Expose
    private String offerCode;
    @SerializedName("discount_value")
    @Expose
    private String discountValue;
    @SerializedName("mer_amount")
    @Expose
    private String merAmount;
    @SerializedName("eci_value")
    @Expose
    private String eciValue;
    @SerializedName("retry")
    @Expose
    private String retry;
    @SerializedName("response_code")
    @Expose
    private String responseCode;
    @SerializedName("billing_notes")
    @Expose
    private String billingNotes;
    @SerializedName("trans_date")
    @Expose
    private String transDate;
    private final static long serialVersionUID = -1309331197292844634L;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getBankRefNo() {
        return bankRefNo;
    }

    public void setBankRefNo(String bankRefNo) {
        this.bankRefNo = bankRefNo;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBillingName() {
        return billingName;
    }

    public void setBillingName(String billingName) {
        this.billingName = billingName;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getBillingCity() {
        return billingCity;
    }

    public void setBillingCity(String billingCity) {
        this.billingCity = billingCity;
    }

    public String getBillingState() {
        return billingState;
    }

    public void setBillingState(String billingState) {
        this.billingState = billingState;
    }

    public String getBillingZip() {
        return billingZip;
    }

    public void setBillingZip(String billingZip) {
        this.billingZip = billingZip;
    }

    public String getBillingCountry() {
        return billingCountry;
    }

    public void setBillingCountry(String billingCountry) {
        this.billingCountry = billingCountry;
    }

    public String getBillingTel() {
        return billingTel;
    }

    public void setBillingTel(String billingTel) {
        this.billingTel = billingTel;
    }

    public String getBillingEmail() {
        return billingEmail;
    }

    public void setBillingEmail(String billingEmail) {
        this.billingEmail = billingEmail;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    public String getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }

    public String getDeliveryZip() {
        return deliveryZip;
    }

    public void setDeliveryZip(String deliveryZip) {
        this.deliveryZip = deliveryZip;
    }

    public String getDeliveryCountry() {
        return deliveryCountry;
    }

    public void setDeliveryCountry(String deliveryCountry) {
        this.deliveryCountry = deliveryCountry;
    }

    public String getDeliveryTel() {
        return deliveryTel;
    }

    public void setDeliveryTel(String deliveryTel) {
        this.deliveryTel = deliveryTel;
    }

    public String getMerchantParam1() {
        return merchantParam1;
    }

    public void setMerchantParam1(String merchantParam1) {
        this.merchantParam1 = merchantParam1;
    }

    public String getMerchantParam2() {
        return merchantParam2;
    }

    public void setMerchantParam2(String merchantParam2) {
        this.merchantParam2 = merchantParam2;
    }

    public String getMerchantParam3() {
        return merchantParam3;
    }

    public void setMerchantParam3(String merchantParam3) {
        this.merchantParam3 = merchantParam3;
    }

    public String getMerchantParam4() {
        return merchantParam4;
    }

    public void setMerchantParam4(String merchantParam4) {
        this.merchantParam4 = merchantParam4;
    }

    public String getMerchantParam5() {
        return merchantParam5;
    }

    public void setMerchantParam5(String merchantParam5) {
        this.merchantParam5 = merchantParam5;
    }

    public String getVault() {
        return vault;
    }

    public void setVault(String vault) {
        this.vault = vault;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public String getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(String discountValue) {
        this.discountValue = discountValue;
    }

    public String getMerAmount() {
        return merAmount;
    }

    public void setMerAmount(String merAmount) {
        this.merAmount = merAmount;
    }

    public String getEciValue() {
        return eciValue;
    }

    public void setEciValue(String eciValue) {
        this.eciValue = eciValue;
    }

    public String getRetry() {
        return retry;
    }

    public void setRetry(String retry) {
        this.retry = retry;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getBillingNotes() {
        return billingNotes;
    }

    public void setBillingNotes(String billingNotes) {
        this.billingNotes = billingNotes;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

}