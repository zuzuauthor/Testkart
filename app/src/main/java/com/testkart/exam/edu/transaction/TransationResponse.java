
package com.testkart.exam.edu.transaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TransationResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("paymentTypeArr")
    @Expose
    private PaymentTypeArr paymentTypeArr;
    @SerializedName("response")
    @Expose
    private List<Response> response = null;
    private final static long serialVersionUID = 3375914391745008915L;

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

    public PaymentTypeArr getPaymentTypeArr() {
        return paymentTypeArr;
    }

    public void setPaymentTypeArr(PaymentTypeArr paymentTypeArr) {
        this.paymentTypeArr = paymentTypeArr;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

}
