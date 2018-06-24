package com.testkart.exam.testkart.my_result.score_card_model;

import java.io.Serializable;
import java.lang.reflect.Field;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by elfemo on 18/8/17.
 */

public class Scorecard implements Serializable
{

    /*@SerializedName("student_count")
    @Expose
    private Integer studentCount;
    @SerializedName("test_marks")
    @Expose
    private String testMarks;
    @SerializedName("total_questions")
    @Expose
    private String totalQuestions;
    @SerializedName("test_time")
    @Expose
    private String testTime;
    @SerializedName("my_marks")
    @Expose
    private String myMarks;
    @SerializedName("my_percentile")
    @Expose
    private Double myPercentile;
    @SerializedName("total_question_attempts")
    @Expose
    private String totalQuestionAttempts;
    @SerializedName("time_taken")
    @Expose
    private String timeTaken;
    @SerializedName("correct_questions")
    @Expose
    private Integer correctQuestions;
    @SerializedName("incorrect_questions")
    @Expose
    private Integer incorrectQuestions;
    @SerializedName("right_marks")
    @Expose
    private String rightMarks;
    @SerializedName("negative_marks")
    @Expose
    private String negativeMarks;
    @SerializedName("unattempt_questions")
    @Expose
    private Integer unattemptQuestions;
    @SerializedName("incorrect_questions_marks")
    @Expose
    private Integer incorrectQuestionsMarks;
    @SerializedName("my_rank")
    @Expose
    private String myRank;
    @SerializedName("my_result")
    @Expose
    private String myResult;
    private final static long serialVersionUID = -108544898463084460L;

    public Integer getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }

    public String getTestMarks() {
        return testMarks;
    }

    public void setTestMarks(String testMarks) {
        this.testMarks = testMarks;
    }

    public String getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(String totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public String getMyMarks() {
        return myMarks;
    }

    public void setMyMarks(String myMarks) {
        this.myMarks = myMarks;
    }

    public Double getMyPercentile() {
        return myPercentile;
    }

    public void setMyPercentile(Double myPercentile) {
        this.myPercentile = myPercentile;
    }

    public String getTotalQuestionAttempts() {
        return totalQuestionAttempts;
    }

    public void setTotalQuestionAttempts(String totalQuestionAttempts) {
        this.totalQuestionAttempts = totalQuestionAttempts;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public Integer getCorrectQuestions() {
        return correctQuestions;
    }

    public void setCorrectQuestions(Integer correctQuestions) {
        this.correctQuestions = correctQuestions;
    }

    public Integer getIncorrectQuestions() {
        return incorrectQuestions;
    }

    public void setIncorrectQuestions(Integer incorrectQuestions) {
        this.incorrectQuestions = incorrectQuestions;
    }

    public String getRightMarks() {
        return rightMarks;
    }

    public void setRightMarks(String rightMarks) {
        this.rightMarks = rightMarks;
    }

    public String getNegativeMarks() {
        return negativeMarks;
    }

    public void setNegativeMarks(String negativeMarks) {
        this.negativeMarks = negativeMarks;
    }

    public Integer getUnattemptQuestions() {
        return unattemptQuestions;
    }

    public void setUnattemptQuestions(Integer unattemptQuestions) {
        this.unattemptQuestions = unattemptQuestions;
    }

    public Integer getIncorrectQuestionsMarks() {
        return incorrectQuestionsMarks;
    }

    public void setIncorrectQuestionsMarks(Integer incorrectQuestionsMarks) {
        this.incorrectQuestionsMarks = incorrectQuestionsMarks;
    }

    public String getMyRank() {
        return myRank;
    }

    public void setMyRank(String myRank) {
        this.myRank = myRank;
    }

    public String getMyResult() {
        return myResult;
    }

    public void setMyResult(String myResult) {
        this.myResult = myResult;
    }*/


    @SerializedName("exam_name")
    @Expose
    private String examName;
    @SerializedName("student_count")
    @Expose
    private String studentCount;
    @SerializedName("test_marks")
    @Expose
    private String testMarks;
    @SerializedName("total_questions")
    @Expose
    private String totalQuestions;
    @SerializedName("test_time")
    @Expose
    private String testTime;
    @SerializedName("my_marks")
    @Expose
    private String myMarks;
    @SerializedName("my_percentile")
    @Expose
    private Double myPercentile;
    @SerializedName("total_question_attempts")
    @Expose
    private String totalQuestionAttempts;
    @SerializedName("time_taken")
    @Expose
    private String timeTaken;
    @SerializedName("correct_questions")
    @Expose
    private Integer correctQuestions;
    @SerializedName("incorrect_questions")
    @Expose
    private Integer incorrectQuestions;
    @SerializedName("right_marks")
    @Expose
    private String rightMarks;
    @SerializedName("negative_marks")
    @Expose
    private String negativeMarks;
    @SerializedName("unattempt_questions")
    @Expose
    private Integer unattemptQuestions;
    @SerializedName("incorrect_questions_marks")
    @Expose
    private String incorrectQuestionsMarks;
    @SerializedName("my_rank")
    @Expose
    private String myRank;
    @SerializedName("my_result")
    @Expose
    private String myResult;
    private final static long serialVersionUID = 2030104910575327531L;

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(String studentCount) {
        this.studentCount = studentCount;
    }

    public String getTestMarks() {
        return testMarks;
    }

    public void setTestMarks(String testMarks) {
        this.testMarks = testMarks;
    }

    public String getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(String totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public String getMyMarks() {
        return myMarks;
    }

    public void setMyMarks(String myMarks) {
        this.myMarks = myMarks;
    }

    public Double getMyPercentile() {
        return myPercentile;
    }

    public void setMyPercentile(Double myPercentile) {
        this.myPercentile = myPercentile;
    }

    public String getTotalQuestionAttempts() {
        return totalQuestionAttempts;
    }

    public void setTotalQuestionAttempts(String totalQuestionAttempts) {
        this.totalQuestionAttempts = totalQuestionAttempts;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public Integer getCorrectQuestions() {
        return correctQuestions;
    }

    public void setCorrectQuestions(Integer correctQuestions) {
        this.correctQuestions = correctQuestions;
    }

    public Integer getIncorrectQuestions() {
        return incorrectQuestions;
    }

    public void setIncorrectQuestions(Integer incorrectQuestions) {
        this.incorrectQuestions = incorrectQuestions;
    }

    public String getRightMarks() {
        return rightMarks;
    }

    public void setRightMarks(String rightMarks) {
        this.rightMarks = rightMarks;
    }

    public String getNegativeMarks() {
        return negativeMarks;
    }

    public void setNegativeMarks(String negativeMarks) {
        this.negativeMarks = negativeMarks;
    }

    public Integer getUnattemptQuestions() {
        return unattemptQuestions;
    }

    public void setUnattemptQuestions(Integer unattemptQuestions) {
        this.unattemptQuestions = unattemptQuestions;
    }

    public String getIncorrectQuestionsMarks() {
        return incorrectQuestionsMarks;
    }

    public void setIncorrectQuestionsMarks(String incorrectQuestionsMarks) {
        this.incorrectQuestionsMarks = incorrectQuestionsMarks;
    }

    public String getMyRank() {
        return myRank;
    }

    public void setMyRank(String myRank) {
        this.myRank = myRank;
    }

    public String getMyResult() {
        return myResult;
    }

    public void setMyResult(String myResult) {
        this.myResult = myResult;
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append( this.getClass().getName() );
        result.append( " Object {" );
        result.append(newLine);

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        //print field names paired with their values
        for ( Field field : fields  ) {
            result.append("  ");
            try {
                result.append( field.getName() );
                result.append(": ");
                //requires access to private field:
                result.append( field.get(this) );
            } catch ( IllegalAccessException ex ) {
                System.out.println(ex);
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }

}