
package com.testkart.exam.edu.exam.ibps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Math implements Serializable
{

    @SerializedName("ExamStat")
    @Expose
    private ExamStat___ examStat;
    private final static long serialVersionUID = -7555635681687958118L;

    public ExamStat___ getExamStat() {
        return examStat;
    }

    public void setExamStat(ExamStat___ examStat) {
        this.examStat = examStat;
    }

}
