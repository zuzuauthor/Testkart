
package com.testkart.exam.testkart.study_material;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudyMaterialResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("study_material")
    @Expose
    private List<StudyMaterial> studyMaterial = null;
    private final static long serialVersionUID = -520098867250869621L;

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

    public List<StudyMaterial> getStudyMaterial() {
        return studyMaterial;
    }

    public void setStudyMaterial(List<StudyMaterial> studyMaterial) {
        this.studyMaterial = studyMaterial;
    }

}
