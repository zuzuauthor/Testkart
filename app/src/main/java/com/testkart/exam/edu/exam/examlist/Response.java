
package com.testkart.exam.edu.exam.examlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Response implements Serializable
{

    @SerializedName("Exam")
    @Expose
    private Exam exam;
    @SerializedName("ExamOrder")
    @Expose
    private ExamOrder examOrder;
    private final static long serialVersionUID = -8647681568795063843L;

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public ExamOrder getExamOrder() {
        return examOrder;
    }

    public void setExamOrder(ExamOrder examOrder) {
        this.examOrder = examOrder;
    }

}
