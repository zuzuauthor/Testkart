
package com.testkart.exam.edu.exam.ibps.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserSectionQuestion implements Serializable
{

    @SerializedName("Chemistry")
    @Expose
    private List<Chemistry> chemistry = null;
    @SerializedName("Physics")
    @Expose
    private List<Physic> physics = null;
    @SerializedName("Maths")
    @Expose
    private List<Math> maths = null;
    private final static long serialVersionUID = -8867316931753678985L;

    public List<Chemistry> getChemistry() {
        return chemistry;
    }

    public void setChemistry(List<Chemistry> chemistry) {
        this.chemistry = chemistry;
    }

    public List<Physic> getPhysics() {
        return physics;
    }

    public void setPhysics(List<Physic> physics) {
        this.physics = physics;
    }

    public List<Math> getMaths() {
        return maths;
    }

    public void setMaths(List<Math> maths) {
        this.maths = maths;
    }

}
