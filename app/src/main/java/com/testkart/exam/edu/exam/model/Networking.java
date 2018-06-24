
package com.testkart.exam.edu.exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Networking {

    @SerializedName("ExamStat")
    @Expose
    private ExamStat_____ examStat;

    public ExamStat_____ getExamStat() {
        return examStat;
    }

    public void setExamStat(ExamStat_____ examStat) {
        this.examStat = examStat;
    }

}
