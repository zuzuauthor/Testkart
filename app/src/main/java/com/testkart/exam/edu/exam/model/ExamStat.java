
package com.testkart.exam.edu.exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExamStat {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("correct_answer")
    @Expose
    private String correctAnswer;
    @SerializedName("exam_result_id")
    @Expose
    private String examResultId;
    @SerializedName("answered")
    @Expose
    private String answered;
    @SerializedName("ques_no")
    @Expose
    private String quesNo;
    @SerializedName("option_selected")
    @Expose
    private Object optionSelected;
    @SerializedName("true_false")
    @Expose
    private Object trueFalse;
    @SerializedName("fill_blank")
    @Expose
    private Object fillBlank;
    @SerializedName("marks")
    @Expose
    private String marks;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("options")
    @Expose
    private String options;
    @SerializedName("answer")
    @Expose
    private Object answer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getExamResultId() {
        return examResultId;
    }

    public void setExamResultId(String examResultId) {
        this.examResultId = examResultId;
    }

    public String getAnswered() {
        return answered;
    }

    public void setAnswered(String answered) {
        this.answered = answered;
    }

    public String getQuesNo() {
        return quesNo;
    }

    public void setQuesNo(String quesNo) {
        this.quesNo = quesNo;
    }

    public Object getOptionSelected() {
        return optionSelected;
    }

    public void setOptionSelected(Object optionSelected) {
        this.optionSelected = optionSelected;
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

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Object getAnswer() {
        return answer;
    }

    public void setAnswer(Object answer) {
        this.answer = answer;
    }

}
