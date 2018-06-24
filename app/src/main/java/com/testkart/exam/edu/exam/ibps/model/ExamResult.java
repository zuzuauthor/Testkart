
package com.testkart.exam.edu.exam.ibps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExamResult implements Serializable
{

    @SerializedName("ExamResult")
    @Expose
    private ExamResult_ examResult;
    private final static long serialVersionUID = 2543939012900984148L;

    public ExamResult_ getExamResult() {
        return examResult;
    }

    public void setExamResult(ExamResult_ examResult) {
        this.examResult = examResult;
    }

}
