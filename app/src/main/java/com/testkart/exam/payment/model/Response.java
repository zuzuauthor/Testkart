
package com.testkart.exam.payment.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response implements Serializable
{

    @SerializedName("Payment")
    @Expose
    private Payment payment;
    @SerializedName("Package")
    @Expose
    private List<Package> _package = null;
    private final static long serialVersionUID = -1436074750902953257L;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public List<Package> getPackage() {
        return _package;
    }

    public void setPackage(List<Package> _package) {
        this._package = _package;
    }

}
