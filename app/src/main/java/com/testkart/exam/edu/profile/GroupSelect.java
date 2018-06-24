
package com.testkart.exam.edu.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GroupSelect implements Serializable
{

    @SerializedName("Groups")
    @Expose
    private Groups groups;
    private final static long serialVersionUID = -5547287099916553446L;

    public Groups getGroups() {
        return groups;
    }

    public void setGroups(Groups groups) {
        this.groups = groups;
    }

}
