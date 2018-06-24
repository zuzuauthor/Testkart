
package com.testkart.exam.banking_digest.buy.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response implements Serializable
{

    @SerializedName("MagazinePackage")
    @Expose
    private MagazinePackage magazinePackage;
    @SerializedName("Magazine")
    @Expose
    private List<Magazine> magazine = null;
    private final static long serialVersionUID = 3321275411310946777L;

    public MagazinePackage getMagazinePackage() {
        return magazinePackage;
    }

    public void setMagazinePackage(MagazinePackage magazinePackage) {
        this.magazinePackage = magazinePackage;
    }

    public List<Magazine> getMagazine() {
        return magazine;
    }

    public void setMagazine(List<Magazine> magazine) {
        this.magazine = magazine;
    }

}
