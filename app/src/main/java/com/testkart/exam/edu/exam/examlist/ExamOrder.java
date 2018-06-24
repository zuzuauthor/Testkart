
package com.testkart.exam.edu.exam.examlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExamOrder implements Serializable
{

    @SerializedName("expiry_date")
    @Expose
    private Object expiryDate;
    private final static long serialVersionUID = -5791255842965092992L;

    public Object getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Object expiryDate) {
        this.expiryDate = expiryDate;
    }

}
