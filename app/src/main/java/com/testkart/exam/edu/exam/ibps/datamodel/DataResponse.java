package com.testkart.exam.edu.exam.ibps.datamodel;

import java.io.Serializable;

/**
 * Created by testkart on 1/7/17.
 */

public class DataResponse implements Serializable{

    private String questionId;
    private String questionLocalId;
    private String subjectId;
    private String subjectName;
    //json format language storage [{"lang_id":"1","lang_name":"Hindi"}]
    private String languageArray;
    private String passageInclude;
    private String marks;
    private String negativeMarks;
    private String correctAnswer;
    private String yourAnswer;
    // multiple = M, Single = W, Fill Blanks = F, Subjective = S, True False = T
    private String questionType;
    private String questionViewTime;
    private String questionSpendTime;
    //opened = 0 / 1, answer = 0 / 1, review = 0 / 1 where 0 = no 1 = yes
    private String opened;
    private String answer;
    private String review;
    private String jumbleOptions;

    public String getQuestionLocalId() {
        return questionLocalId;
    }

    public void setQuestionLocalId(String questionLocalId) {
        this.questionLocalId = questionLocalId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getLanguageArray() {
        return languageArray;
    }

    public void setLanguageArray(String languageArray) {
        this.languageArray = languageArray;
    }

    public String getPassageInclude() {
        return passageInclude;
    }

    public void setPassageInclude(String passageInclude) {
        this.passageInclude = passageInclude;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getNegativeMarks() {
        return negativeMarks;
    }

    public void setNegativeMarks(String negativeMarks) {
        this.negativeMarks = negativeMarks;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getYourAnswer() {
        return yourAnswer;
    }

    public void setYourAnswer(String yourAnswer) {
        this.yourAnswer = yourAnswer;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionViewTime() {
        return questionViewTime;
    }

    public void setQuestionViewTime(String questionViewTime) {
        this.questionViewTime = questionViewTime;
    }

    public String getQuestionSpendTime() {
        return questionSpendTime;
    }

    public void setQuestionSpendTime(String questionSpendTime) {
        this.questionSpendTime = questionSpendTime;
    }

    public String getOpened() {
        return opened;
    }

    public void setOpened(String opened) {
        this.opened = opened;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getJumbleOptions() {
        return jumbleOptions;
    }

    public void setJumbleOptions(String jumbleOptions) {
        this.jumbleOptions = jumbleOptions;
    }
}
