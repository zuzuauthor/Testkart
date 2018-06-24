package com.testkart.exam.testkart.contact_us;

import com.google.gson.annotations.SerializedName;

/**
 * Created by elfemo on 12/8/17.
 */

public class ServerResponse {

    // variable name should be same as in the json response from php
    @SerializedName("success")
    boolean success;
    @SerializedName("message")
    String message;

    String getMessage() {
        return message;
    }

    boolean getSuccess() {
        return success;
    }
}
