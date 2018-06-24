
package com.testkart.exam.edu.exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chemistry {

    @SerializedName("ExamStat")
    @Expose
    private ExamStat__ examStat;

    public ExamStat__ getExamStat() {
        return examStat;
    }

    public void setExamStat(ExamStat__ examStat) {
        this.examStat = examStat;
    }

}
