package com.testkart.exam.edu.exam;

/**
 * Created by testkart on 1/5/17.
 */

public class SubjectModel {

    private String subjectId;
    private String subjectName;
    private String questionsCount;
    private String qIds;

    public String getqIds() {
        return qIds;
    }

    public void setqIds(String qIds) {
        this.qIds = qIds;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getQuestionsCount() {
        return questionsCount;
    }

    public void setQuestionsCount(String questionsCount) {
        this.questionsCount = questionsCount;
    }
}
