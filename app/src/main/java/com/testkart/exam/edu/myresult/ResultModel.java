package com.testkart.exam.edu.myresult;


/**
 * Created by testkart on 23/5/17.
 */

public class ResultModel {

    private String resultId;
    private String examId;
    private String examName;
    private String date;
    private String examResult;
    private String score;
    private String percentage;

    private String resultDeclear;

   private boolean certificate;


    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public boolean isCertificate() {
        return certificate;
    }

    public void setCertificate(boolean certificate) {
        this.certificate = certificate;
    }

    public String getResultDeclear() {
        return resultDeclear;
    }

    public void setResultDeclear(String resultDeclear) {
        this.resultDeclear = resultDeclear;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExamResult() {
        return examResult;
    }

    public void setExamResult(String examResult) {
        this.examResult = examResult;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
