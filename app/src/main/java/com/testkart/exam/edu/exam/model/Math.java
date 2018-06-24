
package com.testkart.exam.edu.exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Math {

    @SerializedName("ExamStat")
    @Expose
    private ExamStat____ examStat;

    public ExamStat____ getExamStat() {
        return examStat;
    }

    public void setExamStat(ExamStat____ examStat) {
        this.examStat = examStat;
    }

}
