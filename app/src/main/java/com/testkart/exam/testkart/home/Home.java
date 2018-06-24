
package com.testkart.exam.testkart.home;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Home implements Serializable
{

    /*@SerializedName("home_data")
    @Expose
    private List<HomeDatum> homeData = null;
    @SerializedName("feature_exam_urls")
    @Expose
    private List<String> featureExamUrls = null;
    private final static long serialVersionUID = 8881547665473209906L;

    public List<HomeDatum> getHomeData() {
        return homeData;
    }

    public void setHomeData(List<HomeDatum> homeData) {
        this.homeData = homeData;
    }

    public List<String> getFeatureExamUrls() {
        return featureExamUrls;
    }

    public void setFeatureExamUrls(List<String> featureExamUrls) {
        this.featureExamUrls = featureExamUrls;
    }
*/


    @SerializedName("home_data")
    @Expose
    private List<HomeDatum> homeData = null;
    @SerializedName("feature_exam_urls")
    @Expose
    private List<String> featureExamUrls = null;
    @SerializedName("testimonials")
    @Expose
    private List<Testimonial> testimonials = null;
    private final static long serialVersionUID = -315948197895010564L;

    public List<HomeDatum> getHomeData() {
        return homeData;
    }

    public void setHomeData(List<HomeDatum> homeData) {
        this.homeData = homeData;
    }

    public List<String> getFeatureExamUrls() {
        return featureExamUrls;
    }

    public void setFeatureExamUrls(List<String> featureExamUrls) {
        this.featureExamUrls = featureExamUrls;
    }

    public List<Testimonial> getTestimonials() {
        return testimonials;
    }

    public void setTestimonials(List<Testimonial> testimonials) {
        this.testimonials = testimonials;
    }

}
