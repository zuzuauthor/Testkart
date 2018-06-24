
package com.testkart.exam.testkart.home;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeDatum implements Serializable
{

    @SerializedName("slides_photo")
    @Expose
    private String slidesPhoto;
    private final static long serialVersionUID = 3022185597402370338L;

    public String getSlidesPhoto() {
        return slidesPhoto;
    }

    public void setSlidesPhoto(String slidesPhoto) {
        this.slidesPhoto = slidesPhoto;
    }

}
