
package com.testkart.exam.banking_digest.my_magazines.model.my_subscription;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Magazine implements Serializable
{

    @SerializedName("magazine_id")
    @Expose
    private String magazineId;
    @SerializedName("magazine_name")
    @Expose
    private String magazineName;
    @SerializedName("magazine_desc")
    @Expose
    private String magazineDesc;
    @SerializedName("magazine_image")
    @Expose
    private String magazineImage;
    private final static long serialVersionUID = 3026116845614056781L;

    public String getMagazineId() {
        return magazineId;
    }

    public void setMagazineId(String magazineId) {
        this.magazineId = magazineId;
    }

    public String getMagazineName() {
        return magazineName;
    }

    public void setMagazineName(String magazineName) {
        this.magazineName = magazineName;
    }

    public String getMagazineDesc() {
        return magazineDesc;
    }

    public void setMagazineDesc(String magazineDesc) {
        this.magazineDesc = magazineDesc;
    }

    public String getMagazineImage() {
        return magazineImage;
    }

    public void setMagazineImage(String magazineImage) {
        this.magazineImage = magazineImage;
    }

}
