
package com.testkart.exam.edu.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterPostModel {

    @SerializedName("responses")
    @Expose
    private Responses responses;

    public Responses getResponses() {
        return responses;
    }

    public void setResponses(Responses responses) {
        this.responses = responses;
    }

}
