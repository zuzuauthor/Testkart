
package com.testkart.exam.packages.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response implements Serializable
{

    @SerializedName("Package")
    @Expose
    private Package _package;
    @SerializedName("Exam")
    @Expose
    private List<Exam> exam = null;
    private final static long serialVersionUID = 6029168407370485882L;

    public Package getPackage() {
        return _package;
    }

    public void setPackage(Package _package) {
        this._package = _package;
    }

    public List<Exam> getExam() {
        return exam;
    }

    public void setExam(List<Exam> exam) {
        this.exam = exam;
    }

}
