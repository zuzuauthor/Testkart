
package com.testkart.exam.edu.leaderboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Selection implements Serializable
{

    @SerializedName("points")
    @Expose
    private String points;
    @SerializedName("student_id")
    @Expose
    private String studentId;
    @SerializedName("exam_given")
    @Expose
    private String examGiven;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("rank")
    @Expose
    private String rank;
    private final static long serialVersionUID = 3450538870888773518L;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getExamGiven() {
        return examGiven;
    }

    public void setExamGiven(String examGiven) {
        this.examGiven = examGiven;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

}
