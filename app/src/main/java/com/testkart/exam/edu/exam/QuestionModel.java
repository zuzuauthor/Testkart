package com.testkart.exam.edu.exam;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by testkart on 1/5/17.
 */

public class QuestionModel {

    private String questionId;
    private String questionNo;
    private String localQuestionId;
    private String subject;
    private String subjectId;
    private String question;
    private boolean isQuestionHtml;
    private ArrayList<String> options;
    private boolean isOptionsHtml;
    private String hint;
    private String marks;
    private String negativeMarks;

    private String questionType;
    private String multipleType; // multiple choice or single choice

    private String trueFalseResponse;
    private String fillBlankResponse;
    private String subjectiveResponse;
    private String multipleChoiceResponse;

    //private boolean isNegativeMarks;
    private String isNegativeMarks;

    private String opened;  //visit or not visit
    private String answered; //
    private String review; //review answered / review only

    private int timeSpendOnQuestion;

    private String attemptTime;

    private String jumbledOptions;

    private HashMap<String, String> questionMap = new HashMap<>();
    private HashMap<String, ArrayList<String>> qptionMap = new HashMap<>();

    public HashMap<String, String> getQuestionMap() {
        return questionMap;
    }

    public void setQuestionMap(HashMap<String, String> questionMap) {
        this.questionMap = questionMap;
    }

    public HashMap<String, ArrayList<String>> getQptionMap() {
        return qptionMap;
    }

    public void setQptionMap(HashMap<String, ArrayList<String>> qptionMap) {
        this.qptionMap = qptionMap;
    }

    public String getJumbledOptions() {
        return jumbledOptions;
    }

    public void setJumbledOptions(String jumbledOptions) {
        this.jumbledOptions = jumbledOptions;
    }

    public String getAttemptTime() {
        return attemptTime;
    }

    public void setAttemptTime(String attemptTime) {
        this.attemptTime = attemptTime;
    }

    public String getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }

    public String getLocalQuestionId() {
        return localQuestionId;
    }

    public void setLocalQuestionId(String localQuestionId) {
        this.localQuestionId = localQuestionId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getIsNegativeMarks() {
        return isNegativeMarks;
    }

    public void setIsNegativeMarks(String isNegativeMarks) {
        this.isNegativeMarks = isNegativeMarks;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isQuestionHtml() {
        return isQuestionHtml;
    }

    public void setQuestionHtml(boolean questionHtml) {
        isQuestionHtml = questionHtml;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public boolean isOptionsHtml() {
        return isOptionsHtml;
    }

    public void setOptionsHtml(boolean optionsHtml) {
        isOptionsHtml = optionsHtml;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
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

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getMultipleType() {
        return multipleType;
    }

    public void setMultipleType(String multipleType) {
        this.multipleType = multipleType;
    }

    public String getTrueFalseResponse() {
        return trueFalseResponse;
    }

    public void setTrueFalseResponse(String trueFalseResponse) {
        this.trueFalseResponse = trueFalseResponse;
    }

    public String getFillBlankResponse() {
        return fillBlankResponse;
    }

    public void setFillBlankResponse(String fillBlankResponse) {
        this.fillBlankResponse = fillBlankResponse;
    }

    public String getSubjectiveResponse() {
        return subjectiveResponse;
    }

    public void setSubjectiveResponse(String subjectiveResponse) {
        this.subjectiveResponse = subjectiveResponse;
    }

    public String getMultipleChoiceResponse() {
        return multipleChoiceResponse;
    }

    public void setMultipleChoiceResponse(String multipleChoiceResponse) {
        this.multipleChoiceResponse = multipleChoiceResponse;
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

    public int getTimeSpendOnQuestion() {
        return timeSpendOnQuestion;
    }

    public void setTimeSpendOnQuestion(int timeSpendOnQuestion) {
        this.timeSpendOnQuestion = timeSpendOnQuestion;
    }


    public String toString(QuestionModel abc) {

        try{

            System.out.println("Question ------------------START");

            for (Field field : abc.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                String name = field.getName();
                Object value = field.get(abc);
                System.out.printf("Field name: %s, Field value: %s%n", name, value);
            }

        }catch (IllegalAccessException e){

            e.printStackTrace();
        }

        return "Question ------------------END ";
    }


}
