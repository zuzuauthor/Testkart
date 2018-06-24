
package com.testkart.exam.edu.exam.ibps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Physic implements Serializable
{

    @SerializedName("ExamStat")
    @Expose
    private ExamStat__ examStat;
    private final static long serialVersionUID = -818271956392032774L;

    public ExamStat__ getExamStat() {
        return examStat;
    }

    public void setExamStat(ExamStat__ examStat) {
        this.examStat = examStat;
    }

}
