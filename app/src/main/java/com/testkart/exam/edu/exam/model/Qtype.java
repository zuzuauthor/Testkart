
package com.testkart.exam.edu.exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Qtype {

    @SerializedName("type")
    @Expose
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
