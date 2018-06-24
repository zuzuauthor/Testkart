
package com.testkart.exam.edu.exam.ibps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Qtype implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("question_type")
    @Expose
    private String questionType;
    @SerializedName("type")
    @Expose
    private String type;
    private final static long serialVersionUID = 5766701842490707259L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
