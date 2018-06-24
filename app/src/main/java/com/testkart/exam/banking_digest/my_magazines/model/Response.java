
package com.testkart.exam.banking_digest.my_magazines.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response implements Serializable
{

    @SerializedName("Magazine")
    @Expose
    private Magazine magazine;
    @SerializedName("Post")
    @Expose
    private List<Post> post = null;
    private final static long serialVersionUID = -4894868252029722960L;

    public Magazine getMagazine() {
        return magazine;
    }

    public void setMagazine(Magazine magazine) {
        this.magazine = magazine;
    }

    public List<Post> getPost() {
        return post;
    }

    public void setPost(List<Post> post) {
        this.post = post;
    }

}
