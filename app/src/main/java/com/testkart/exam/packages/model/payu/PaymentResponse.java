package com.testkart.exam.packages.model.payu;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by elfemo on 12/9/17.
 */

public class PaymentResponse implements Serializable
{

    @SerializedName("id")
    @Expose
    private Double id;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("unmappedstatus")
    @Expose
    private String unmappedstatus;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("txnid")
    @Expose
    private String txnid;
    @SerializedName("transaction_fee")
    @Expose
    private String transactionFee;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("cardCategory")
    @Expose
    private String cardCategory;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("additional_charges")
    @Expose
    private String additionalCharges;
    @SerializedName("addedon")
    @Expose
    private String addedon;
    @SerializedName("productinfo")
    @Expose
    private String productinfo;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("field7")
    @Expose
    private String field7;
    @SerializedName("field8")
    @Expose
    private String field8;
    @SerializedName("field9")
    @Expose
    private String field9;
    @SerializedName("payment_source")
    @Expose
    private String paymentSource;
    @SerializedName("PG_TYPE")
    @Expose
    private String pGTYPE;
    @SerializedName("ibibo_code")
    @Expose
    private String ibiboCode;
    @SerializedName("error_code")
    @Expose
    private String errorCode;
    @SerializedName("Error_Message")
    @Expose
    private String errorMessage;
    @SerializedName("name_on_card")
    @Expose
    private String nameOnCard;
    @SerializedName("card_no")
    @Expose
    private String cardNo;
    @SerializedName("is_seamless")
    @Expose
    private Integer isSeamless;
    @SerializedName("surl")
    @Expose
    private String surl;
    @SerializedName("furl")
    @Expose
    private String furl;


    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private final static long serialVersionUID = 4654961803969562871L;

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnmappedstatus() {
        return unmappedstatus;
    }

    public void setUnmappedstatus(String unmappedstatus) {
        this.unmappedstatus = unmappedstatus;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(String transactionFee) {
        this.transactionFee = transactionFee;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCardCategory() {
        return cardCategory;
    }

    public void setCardCategory(String cardCategory) {
        this.cardCategory = cardCategory;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(String additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public String getAddedon() {
        return addedon;
    }

    public void setAddedon(String addedon) {
        this.addedon = addedon;
    }

    public String getProductinfo() {
        return productinfo;
    }

    public void setProductinfo(String productinfo) {
        this.productinfo = productinfo;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getField7() {
        return field7;
    }

    public void setField7(String field7) {
        this.field7 = field7;
    }

    public String getField8() {
        return field8;
    }

    public void setField8(String field8) {
        this.field8 = field8;
    }

    public String getField9() {
        return field9;
    }

    public void setField9(String field9) {
        this.field9 = field9;
    }

    public String getPaymentSource() {
        return paymentSource;
    }

    public void setPaymentSource(String paymentSource) {
        this.paymentSource = paymentSource;
    }

    public String getPGTYPE() {
        return pGTYPE;
    }

    public void setPGTYPE(String pGTYPE) {
        this.pGTYPE = pGTYPE;
    }

    public String getIbiboCode() {
        return ibiboCode;
    }

    public void setIbiboCode(String ibiboCode) {
        this.ibiboCode = ibiboCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Integer getIsSeamless() {
        return isSeamless;
    }

    public void setIsSeamless(Integer isSeamless) {
        this.isSeamless = isSeamless;
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }

    public String getFurl() {
        return furl;
    }

    public void setFurl(String furl) {
        this.furl = furl;
    }

}