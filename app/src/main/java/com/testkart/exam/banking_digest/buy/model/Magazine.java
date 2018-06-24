
package com.testkart.exam.banking_digest.buy.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Magazine implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("files")
    @Expose
    private String files;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("ordering")
    @Expose
    private String ordering;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("MagazinesPackage")
    @Expose
    private MagazinesPackage magazinesPackage;
    private final static long serialVersionUID = -8087319081873696717L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrdering() {
        return ordering;
    }

    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public MagazinesPackage getMagazinesPackage() {
        return magazinesPackage;
    }

    public void setMagazinesPackage(MagazinesPackage magazinesPackage) {
        this.magazinesPackage = magazinesPackage;
    }

}
