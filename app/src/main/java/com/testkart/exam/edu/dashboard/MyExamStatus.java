
package com.testkart.exam.edu.dashboard;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyExamStatus implements Serializable
{

    @SerializedName("total_exam_given")
    @Expose
    private Integer totalExamGiven;
    @SerializedName("absent_exams")
    @Expose
    private Integer absentExams;
    @SerializedName("best_score_in")
    @Expose
    private String bestScoreIn;
    @SerializedName("on")
    @Expose
    private String on;
    @SerializedName("failed_exam_count")
    @Expose
    private Integer failedExamCount;
    @SerializedName("average_percentage")
    @Expose
    private String averagePercentage;
    @SerializedName("rank")
    @Expose
    private String rank;
    private final static long serialVersionUID = 8248878463210240678L;

    public Integer getTotalExamGiven() {
        return totalExamGiven;
    }

    public void setTotalExamGiven(Integer totalExamGiven) {
        this.totalExamGiven = totalExamGiven;
    }

    public Integer getAbsentExams() {
        return absentExams;
    }

    public void setAbsentExams(Integer absentExams) {
        this.absentExams = absentExams;
    }

    public String getBestScoreIn() {
        return bestScoreIn;
    }

    public void setBestScoreIn(String bestScoreIn) {
        this.bestScoreIn = bestScoreIn;
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }

    public Integer getFailedExamCount() {
        return failedExamCount;
    }

    public void setFailedExamCount(Integer failedExamCount) {
        this.failedExamCount = failedExamCount;
    }

    public String getAveragePercentage() {
        return averagePercentage;
    }

    public void setAveragePercentage(String averagePercentage) {
        this.averagePercentage = averagePercentage;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }


    @Override
    public String toString() {

        String str = "totalExamGiven: "+totalExamGiven.intValue()+"\n"+

                "absentExams: "+absentExams.intValue()+"\n"+
                "bestScoreIn: "+bestScoreIn+"\n"+
                "on: "+on+"\n"+
                "failedExamCount: "+failedExamCount+"\n"+
                "averagePercentage: "+averagePercentage+"\n"+
                "rank: "+rank+"\n";

        return str;
    }
}
