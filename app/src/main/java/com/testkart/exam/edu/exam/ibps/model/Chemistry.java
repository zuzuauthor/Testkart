
package com.testkart.exam.edu.exam.ibps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chemistry implements Serializable
{

    @SerializedName("ExamStat")
    @Expose
    private ExamStat_ examStat;
    private final static long serialVersionUID = 2277358103919825327L;

    public ExamStat_ getExamStat() {
        return examStat;
    }

    public void setExamStat(ExamStat_ examStat) {
        this.examStat = examStat;
    }

}
