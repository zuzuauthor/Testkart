package com.testkart.exam.edu.leaderboard;

/**
 * Created by testkart on 18/5/17.
 */

class LeaderboardModel {


    private String studentName;
    private String rank;
    private String avgPercentage;
    private String totalExam;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getAvgPercentage() {
        return avgPercentage;
    }

    public void setAvgPercentage(String avgPercentage) {
        this.avgPercentage = avgPercentage;
    }

    public String getTotalExam() {
        return totalExam;
    }

    public void setTotalExam(String totalExam) {
        this.totalExam = totalExam;
    }
}
