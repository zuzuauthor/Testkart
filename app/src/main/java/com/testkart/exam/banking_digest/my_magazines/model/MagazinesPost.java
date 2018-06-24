
package com.testkart.exam.banking_digest.my_magazines.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MagazinesPost implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("magazine_id")
    @Expose
    private String magazineId;
    @SerializedName("post_id")
    @Expose
    private String postId;
    private final static long serialVersionUID = 8639453668842226676L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMagazineId() {
        return magazineId;
    }

    public void setMagazineId(String magazineId) {
        this.magazineId = magazineId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

}
