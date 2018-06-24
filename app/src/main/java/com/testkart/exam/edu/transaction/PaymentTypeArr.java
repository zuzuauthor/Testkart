
package com.testkart.exam.edu.transaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentTypeArr implements Serializable
{

    @SerializedName("AD")
    @Expose
    private String aD;
    @SerializedName("PG")
    @Expose
    private String pG;
    @SerializedName("EM")
    @Expose
    private String eM;
    private final static long serialVersionUID = 2963484277437628118L;

    public String getAD() {
        return aD;
    }

    public void setAD(String aD) {
        this.aD = aD;
    }

    public String getPG() {
        return pG;
    }

    public void setPG(String pG) {
        this.pG = pG;
    }

    public String getEM() {
        return eM;
    }

    public void setEM(String eM) {
        this.eM = eM;
    }

}
