
package com.testkart.exam.edu.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Groups implements Serializable
{

    @SerializedName("group_name")
    @Expose
    private String groupName;
    private final static long serialVersionUID = -497392374768506566L;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
