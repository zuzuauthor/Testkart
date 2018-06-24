
package com.testkart.exam.banking_digest.my_magazines.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Magazine implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("ordering")
    @Expose
    private String ordering;
    @SerializedName("fexpiry_date")
    @Expose
    private String fexpiryDate;
    private final static long serialVersionUID = -8251628927193160472L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getOrdering() {
        return ordering;
    }

    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }

    public String getFexpiryDate() {
        return fexpiryDate;
    }

    public void setFexpiryDate(String fexpiryDate) {
        this.fexpiryDate = fexpiryDate;
    }

}
