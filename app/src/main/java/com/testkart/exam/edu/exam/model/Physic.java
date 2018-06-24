
package com.testkart.exam.edu.exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Physic {

    @SerializedName("ExamStat")
    @Expose
    private ExamStat______ examStat;

    public ExamStat______ getExamStat() {
        return examStat;
    }

    public void setExamStat(ExamStat______ examStat) {
        this.examStat = examStat;
    }

}
