package com.testkart.exam.edu.exam.feedback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by testkart on 19/5/17.
 */

public class SubmitFeedbackResponse {

    @SerializedName("exam_result_id")
    @Expose
    private Integer examResultId;
    @SerializedName("public_key")
    @Expose
    private String publicKey;
    @SerializedName("private_key")
    @Expose
    private String privateKey;
    @SerializedName("responses")
    @Expose
    private Responses responses;
    private final static long serialVersionUID = 1767289310729854591L;

    public Integer getExamResultId() {
        return examResultId;
    }

    public void setExamResultId(Integer examResultId) {
        this.examResultId = examResultId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public Responses getResponses() {
        return responses;
    }

    public void setResponses(Responses responses) {
        this.responses = responses;
    }
}
