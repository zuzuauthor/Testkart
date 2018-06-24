
package com.testkart.exam.edu.dashboard;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardData implements Serializable
{

    @SerializedName("my_exam_status")
    @Expose
    private MyExamStatus myExamStatus;
    @SerializedName("todays_exams")
    @Expose
    private List<TodaysExam> todaysExams = null;
    private final static long serialVersionUID = 8374588537902588648L;

    public MyExamStatus getMyExamStatus() {
        return myExamStatus;
    }

    public void setMyExamStatus(MyExamStatus myExamStatus) {
        this.myExamStatus = myExamStatus;
    }

    public List<TodaysExam> getTodaysExams() {
        return todaysExams;
    }

    public void setTodaysExams(List<TodaysExam> todaysExams) {
        this.todaysExams = todaysExams;
    }

}
