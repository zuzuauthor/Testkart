package com.testkart.exam.testkart.my_result.subject_report;

import java.io.Serializable;
import java.lang.reflect.Field;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by elfemo on 18/8/17.
 */

public class GrandTotal implements Serializable
{

    /*@SerializedName("total_questions")
    @Expose
    private Integer totalQuestions;
    @SerializedName("correct_incorrect_count")
    @Expose
    private String correctIncorrectCount;
    @SerializedName("positive_negative_marks")
    @Expose
    private String positiveNegativeMarks;
    @SerializedName("unattempt_question_count_marks")
    @Expose
    private String unattemptQuestionCountMarks;


    @SerializedName("exam_name")
    @Expose
    private String examName;

    private final static long serialVersionUID = -390538456607309634L;

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getCorrectIncorrectCount() {
        return correctIncorrectCount;
    }

    public void setCorrectIncorrectCount(String correctIncorrectCount) {
        this.correctIncorrectCount = correctIncorrectCount;
    }

    public String getPositiveNegativeMarks() {
        return positiveNegativeMarks;
    }

    public void setPositiveNegativeMarks(String positiveNegativeMarks) {
        this.positiveNegativeMarks = positiveNegativeMarks;
    }

    public String getUnattemptQuestionCountMarks() {
        return unattemptQuestionCountMarks;
    }

    public void setUnattemptQuestionCountMarks(String unattemptQuestionCountMarks) {
        this.unattemptQuestionCountMarks = unattemptQuestionCountMarks;
    }*/


    @SerializedName("exam_name")
    @Expose
    private String examName;
    @SerializedName("total_questions")
    @Expose
    private Integer totalQuestions;
    @SerializedName("correct_incorrect_count")
    @Expose
    private String correctIncorrectCount;
    @SerializedName("positive_negative_marks")
    @Expose
    private String positiveNegativeMarks;
    @SerializedName("unattempt_question_count_marks")
    @Expose
    private String unattemptQuestionCountMarks;
    private final static long serialVersionUID = 9067414640419283992L;

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getCorrectIncorrectCount() {
        return correctIncorrectCount;
    }

    public void setCorrectIncorrectCount(String correctIncorrectCount) {
        this.correctIncorrectCount = correctIncorrectCount;
    }

    public String getPositiveNegativeMarks() {
        return positiveNegativeMarks;
    }

    public void setPositiveNegativeMarks(String positiveNegativeMarks) {
        this.positiveNegativeMarks = positiveNegativeMarks;
    }

    public String getUnattemptQuestionCountMarks() {
        return unattemptQuestionCountMarks;
    }

    public void setUnattemptQuestionCountMarks(String unattemptQuestionCountMarks) {
        this.unattemptQuestionCountMarks = unattemptQuestionCountMarks;
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
