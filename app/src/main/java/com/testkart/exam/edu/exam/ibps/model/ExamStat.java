
package com.testkart.exam.edu.exam.ibps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExamStat implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("exam_result_id")
    @Expose
    private String examResultId;
    @SerializedName("exam_id")
    @Expose
    private String examId;
    @SerializedName("student_id")
    @Expose
    private String studentId;
    @SerializedName("question_id")
    @Expose
    private String questionId;
    @SerializedName("ques_no")
    @Expose
    private String quesNo;
    @SerializedName("options")
    @Expose
    private String options;
    @SerializedName("attempt_time")
    @Expose
    private Object attemptTime;
    @SerializedName("opened")
    @Expose
    private String opened;
    @SerializedName("answered")
    @Expose
    private String answered;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("option_selected")
    @Expose
    private Object optionSelected;
    @SerializedName("answer")
    @Expose
    private Object answer;
    @SerializedName("true_false")
    @Expose
    private Object trueFalse;
    @SerializedName("fill_blank")
    @Expose
    private Object fillBlank;
    @SerializedName("correct_answer")
    @Expose
    private String correctAnswer;
    @SerializedName("marks")
    @Expose
    private String marks;
    @SerializedName("marks_obtained")
    @Expose
    private Object marksObtained;
    @SerializedName("ques_status")
    @Expose
    private Object quesStatus;
    @SerializedName("closed")
    @Expose
    private String closed;
    @SerializedName("user_id")
    @Expose
    private Object userId;
    @SerializedName("checking_time")
    @Expose
    private Object checkingTime;
    @SerializedName("time_taken")
    @Expose
    private Object timeTaken;
    @SerializedName("bookmark")
    @Expose
    private Object bookmark;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    private final static long serialVersionUID = -6860451009519599965L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExamResultId() {
        return examResultId;
    }

    public void setExamResultId(String examResultId) {
        this.examResultId = examResultId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuesNo() {
        return quesNo;
    }

    public void setQuesNo(String quesNo) {
        this.quesNo = quesNo;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Object getAttemptTime() {
        return attemptTime;
    }

    public void setAttemptTime(Object attemptTime) {
        this.attemptTime = attemptTime;
    }

    public String getOpened() {
        return opened;
    }

    public void setOpened(String opened) {
        this.opened = opened;
    }

    public String getAnswered() {
        return answered;
    }

    public void setAnswered(String answered) {
        this.answered = answered;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Object getOptionSelected() {
        return optionSelected;
    }

    public void setOptionSelected(Object optionSelected) {
        this.optionSelected = optionSelected;
    }

    public Object getAnswer() {
        return answer;
    }

    public void setAnswer(Object answer) {
        this.answer = answer;
    }

    public Object getTrueFalse() {
        return trueFalse;
    }

    public void setTrueFalse(Object trueFalse) {
        this.trueFalse = trueFalse;
    }

    public Object getFillBlank() {
        return fillBlank;
    }

    public void setFillBlank(Object fillBlank) {
        this.fillBlank = fillBlank;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public Object getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(Object marksObtained) {
        this.marksObtained = marksObtained;
    }

    public Object getQuesStatus() {
        return quesStatus;
    }

    public void setQuesStatus(Object quesStatus) {
        this.quesStatus = quesStatus;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Object getCheckingTime() {
        return checkingTime;
    }

    public void setCheckingTime(Object checkingTime) {
        this.checkingTime = checkingTime;
    }

    public Object getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Object timeTaken) {
        this.timeTaken = timeTaken;
    }

    public Object getBookmark() {
        return bookmark;
    }

    public void setBookmark(Object bookmark) {
        this.bookmark = bookmark;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

}
