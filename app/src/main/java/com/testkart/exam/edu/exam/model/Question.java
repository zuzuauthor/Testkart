
package com.testkart.exam.edu.exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("option1")
    @Expose
    private String option1;
    @SerializedName("option2")
    @Expose
    private String option2;
    @SerializedName("option3")
    @Expose
    private String option3;
    @SerializedName("option4")
    @Expose
    private String option4;
    @SerializedName("option5")
    @Expose
    private String option5;
    @SerializedName("option6")
    @Expose
    private String option6;
    @SerializedName("negative_marks")
    @Expose
    private String negativeMarks;
    @SerializedName("answer")
    @Expose
    private String answer;
    @SerializedName("true_false")
    @Expose
    private Object trueFalse;
    @SerializedName("fill_blank")
    @Expose
    private String fillBlank;
    @SerializedName("hint")
    @Expose
    private String hint;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getOption5() {
        return option5;
    }

    public void setOption5(String option5) {
        this.option5 = option5;
    }

    public String getOption6() {
        return option6;
    }

    public void setOption6(String option6) {
        this.option6 = option6;
    }

    public String getNegativeMarks() {
        return negativeMarks;
    }

    public void setNegativeMarks(String negativeMarks) {
        this.negativeMarks = negativeMarks;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Object getTrueFalse() {
        return trueFalse;
    }

    public void setTrueFalse(Object trueFalse) {
        this.trueFalse = trueFalse;
    }

    public String getFillBlank() {
        return fillBlank;
    }

    public void setFillBlank(String fillBlank) {
        this.fillBlank = fillBlank;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

}
