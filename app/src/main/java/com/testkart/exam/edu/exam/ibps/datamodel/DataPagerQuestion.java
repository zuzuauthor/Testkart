package com.testkart.exam.edu.exam.ibps.datamodel;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by testkart on 5/7/17.
 */

public class DataPagerQuestion implements Serializable {

    private DataResponse questionStat;
    private String examId;
    private HashMap<String, DataQuestion> questionMap = new HashMap<>();

    public DataResponse getQuestionStat() {
        return questionStat;
    }

    public void setQuestionStat(DataResponse questionStat) {
        this.questionStat = questionStat;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public HashMap<String, DataQuestion> getQuestionMap() {
        return questionMap;
    }

    public void setQuestionMap(HashMap<String, DataQuestion> questionMap) {
        this.questionMap = questionMap;
    }
}
