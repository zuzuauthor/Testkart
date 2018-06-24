
package com.testkart.exam.edu.exam.ibps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserExamQuestion implements Serializable
{

    @SerializedName("ExamStat")
    @Expose
    private ExamStat examStat;
    @SerializedName("Question")
    @Expose
    private Question question;
    @SerializedName("Exam")
    @Expose
    private Exam exam;
    private final static long serialVersionUID = 6975390698373241613L;

    public ExamStat getExamStat() {
        return examStat;
    }

    public void setExamStat(ExamStat examStat) {
        this.examStat = examStat;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

}
