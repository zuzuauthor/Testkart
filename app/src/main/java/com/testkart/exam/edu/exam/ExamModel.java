package com.testkart.exam.edu.exam;

import java.io.Serializable;

/**
 * Created by testkart on 1/5/17.
 */

public class ExamModel implements Serializable{

    private String examId;
    private String examName;
    private String examDetails;
    private String duration;
    private String startDate;
    private String endDate;
    private String passingPercent;
    private String negativeMarking;
    private String paidExam;
    private String amount;
    private String examStartTime;
    private String examEndTime;
    private String examFinilize;
    private String examDurationContinue;

    public String getExamDurationContinue() {
        return examDurationContinue;
    }

    public void setExamDurationContinue(String examDurationContinue) {
        this.examDurationContinue = examDurationContinue;
    }

    public String getExamFinilize() {
        return examFinilize;
    }

    public void setExamFinilize(String examFinilize) {
        this.examFinilize = examFinilize;
    }

    public String getExamStartTime() {
        return examStartTime;
    }

    public void setExamStartTime(String examStartTime) {
        this.examStartTime = examStartTime;
    }

    public String getExamEndTime() {
        return examEndTime;
    }

    public void setExamEndTime(String examEndTime) {
        this.examEndTime = examEndTime;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamDetails() {
        return examDetails;
    }

    public void setExamDetails(String examDetails) {
        this.examDetails = examDetails;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPassingPercent() {
        return passingPercent;
    }

    public void setPassingPercent(String passingPercent) {
        this.passingPercent = passingPercent;
    }

    public String getNegativeMarking() {
        return negativeMarking;
    }

    public void setNegativeMarking(String negativeMarking) {
        this.negativeMarking = negativeMarking;
    }

    public String getPaidExam() {
        return paidExam;
    }

    public void setPaidExam(String paidExam) {
        this.paidExam = paidExam;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }



    /*

{
    student_id:   id,
    exam_id:      2,
    start_time:   in timestamp,
    end_time:     in timestamp,
    public_key: hahah
    private_key: lolo

    responses:     [

                        {

                          question_id : 1,
                          question_type : M,
                          response : 1,2,
                          question_status: 0,
                          "attempt_time": "2017-05-11 17:47:49",
                          "opened": "1",
                          "answered": "1",
                           "review": "0",
                           "time_taken": "20"

                        },

                        {

                          question_id : 2,
                          question_type : S,
                          response : Hello how are you,
                          question_status: 0,
                          "attempt_time": "2017-05-11 17:47:49",
                          "opened": "1",
                            "answered": "1",
                           "review": "0",
                          "time_taken": "20"

                        }

                   ]

}

     */


}
