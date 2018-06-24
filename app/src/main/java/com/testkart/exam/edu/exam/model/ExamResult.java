
package com.testkart.exam.edu.exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExamResult {

    @SerializedName("ExamResult")
    @Expose
    private ExamResult_ examResult;

    public ExamResult_ getExamResult() {
        return examResult;
    }

    public void setExamResult(ExamResult_ examResult) {
        this.examResult = examResult;
    }

}
