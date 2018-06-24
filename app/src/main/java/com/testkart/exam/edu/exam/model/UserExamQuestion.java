
package com.testkart.exam.edu.exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserExamQuestion {

    @SerializedName("ExamStat")
    @Expose
    private ExamStat examStat;
    @SerializedName("Exam")
    @Expose
    private Exam exam;
    @SerializedName("Subject")
    @Expose
    private Subject subject;
    @SerializedName("Question")
    @Expose
    private Question question;
    @SerializedName("Qtype")
    @Expose
    private Qtype qtype;

    public ExamStat getExamStat() {
        return examStat;
    }

    public void setExamStat(ExamStat examStat) {
        this.examStat = examStat;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Qtype getQtype() {
        return qtype;
    }

    public void setQtype(Qtype qtype) {
        this.qtype = qtype;
    }

}
