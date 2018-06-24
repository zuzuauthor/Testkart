
package com.testkart.exam.edu.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProfileResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response")
    @Expose
    private Response response;
    @SerializedName("studentImage")
    @Expose
    private String studentImage;
    @SerializedName("groupSelect")
    @Expose
    private List<GroupSelect> groupSelect = null;
    private final static long serialVersionUID = -2472514076434034521L;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getStudentImage() {
        return studentImage;
    }

    public void setStudentImage(String studentImage) {
        this.studentImage = studentImage;
    }

    public List<GroupSelect> getGroupSelect() {
        return groupSelect;
    }

    public void setGroupSelect(List<GroupSelect> groupSelect) {
        this.groupSelect = groupSelect;
    }

}
