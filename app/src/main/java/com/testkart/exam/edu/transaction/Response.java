
package com.testkart.exam.edu.transaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Response implements Serializable
{

    @SerializedName("Transactionhistory")
    @Expose
    private Transactionhistory transactionhistory;
    private final static long serialVersionUID = -4783815275503258443L;

    public Transactionhistory getTransactionhistory() {
        return transactionhistory;
    }

    public void setTransactionhistory(Transactionhistory transactionhistory) {
        this.transactionhistory = transactionhistory;
    }

}
