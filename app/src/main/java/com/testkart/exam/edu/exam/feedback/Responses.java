package com.testkart.exam.edu.exam.feedback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by testkart on 19/5/17.
 */

public class Responses {

    @SerializedName("comment1")
    @Expose
    private String comment1;
    @SerializedName("comment2")
    @Expose
    private String comment2;
    @SerializedName("comment3")
    @Expose
    private String comment3;
    @SerializedName("comments")
    @Expose
    private String comments;
    private final static long serialVersionUID = 5590940003800386165L;

    public String getComment1() {
        return comment1;
    }

    public void setComment1(String comment1) {
        this.comment1 = comment1;
    }

    public String getComment2() {
        return comment2;
    }

    public void setComment2(String comment2) {
        this.comment2 = comment2;
    }

    public String getComment3() {
        return comment3;
    }

    public void setComment3(String comment3) {
        this.comment3 = comment3;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
