
package com.testkart.exam.edu.exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserSectionQuestion {

    @SerializedName("Android")
    @Expose
    private List<Android> android = null;
    @SerializedName("Chemistry")
    @Expose
    private List<Chemistry> chemistry = null;
    @SerializedName("Java")
    @Expose
    private List<Java> java = null;
    @SerializedName("Maths")
    @Expose
    private List<Math> maths = null;
    @SerializedName("Networking")
    @Expose
    private List<Networking> networking = null;
    @SerializedName("Physics")
    @Expose
    private List<Physic> physics = null;

    public List<Android> getAndroid() {
        return android;
    }

    public void setAndroid(List<Android> android) {
        this.android = android;
    }

    public List<Chemistry> getChemistry() {
        return chemistry;
    }

    public void setChemistry(List<Chemistry> chemistry) {
        this.chemistry = chemistry;
    }

    public List<Java> getJava() {
        return java;
    }

    public void setJava(List<Java> java) {
        this.java = java;
    }

    public List<Math> getMaths() {
        return maths;
    }

    public void setMaths(List<Math> maths) {
        this.maths = maths;
    }

    public List<Networking> getNetworking() {
        return networking;
    }

    public void setNetworking(List<Networking> networking) {
        this.networking = networking;
    }

    public List<Physic> getPhysics() {
        return physics;
    }

    public void setPhysics(List<Physic> physics) {
        this.physics = physics;
    }

}
