
package com.testkart.exam.edu.exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Android {

    @SerializedName("ExamStat")
    @Expose
    private ExamStat_ examStat;

    public ExamStat_ getExamStat() {
        return examStat;
    }

    public void setExamStat(ExamStat_ examStat) {
        this.examStat = examStat;
    }

}
