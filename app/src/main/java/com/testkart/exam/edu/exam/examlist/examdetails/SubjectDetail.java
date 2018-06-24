
package com.testkart.exam.edu.exam.examlist.examdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubjectDetail implements Serializable
{

    @SerializedName("Android")
    @Expose
    private Android android;
    @SerializedName("Chemistry")
    @Expose
    private Chemistry chemistry;
    @SerializedName("Java")
    @Expose
    private Java java;
    @SerializedName("Maths")
    @Expose
    private Maths maths;
    @SerializedName("Networking")
    @Expose
    private Networking networking;
    @SerializedName("Physics")
    @Expose
    private Physics physics;
    private final static long serialVersionUID = -3803606365722291118L;

    public Android getAndroid() {
        return android;
    }

    public void setAndroid(Android android) {
        this.android = android;
    }

    public Chemistry getChemistry() {
        return chemistry;
    }

    public void setChemistry(Chemistry chemistry) {
        this.chemistry = chemistry;
    }

    public Java getJava() {
        return java;
    }

    public void setJava(Java java) {
        this.java = java;
    }

    public Maths getMaths() {
        return maths;
    }

    public void setMaths(Maths maths) {
        this.maths = maths;
    }

    public Networking getNetworking() {
        return networking;
    }

    public void setNetworking(Networking networking) {
        this.networking = networking;
    }

    public Physics getPhysics() {
        return physics;
    }

    public void setPhysics(Physics physics) {
        this.physics = physics;
    }

}
