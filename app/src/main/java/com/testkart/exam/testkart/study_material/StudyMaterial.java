
package com.testkart.exam.testkart.study_material;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudyMaterial implements Serializable
{

    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("file_url")
    @Expose
    private String fileUrl;
    @SerializedName("file_type")
    @Expose
    private String fileType;
    @SerializedName("file_icon_url")
    @Expose
    private String fileIconUrl;
    private final static long serialVersionUID = 6333155092340071284L;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileIconUrl() {
        return fileIconUrl;
    }

    public void setFileIconUrl(String fileIconUrl) {
        this.fileIconUrl = fileIconUrl;
    }

}
