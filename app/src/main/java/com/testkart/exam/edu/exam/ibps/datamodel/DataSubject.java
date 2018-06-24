package com.testkart.exam.edu.exam.ibps.datamodel;

import java.io.Serializable;

/**
 * Created by testkart on 1/7/17.
 */

public class DataSubject implements Serializable {

    private String subjectId;
    private String subjectName;
    private String ordering;

    public String getOrdering() {
        return ordering;
    }

    public void setOrdering(String ordering) {
        this.ordering = ordering;
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
}
