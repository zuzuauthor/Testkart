package com.testkart.exam.testkart.my_result.subject_report;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by elfemo on 18/8/17.
 */

public class SubjectReport implements Serializable
{

    @SerializedName("Subject_stats")
    @Expose
    private List<SubjectStat> subjectStats = null;
    @SerializedName("Grand_total")
    @Expose
    private GrandTotal grandTotal;
    private final static long serialVersionUID = 8212858744142756819L;

    public List<SubjectStat> getSubjectStats() {
        return subjectStats;
    }

    public void setSubjectStats(List<SubjectStat> subjectStats) {
        this.subjectStats = subjectStats;
    }

    public GrandTotal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(GrandTotal grandTotal) {
        this.grandTotal = grandTotal;
    }

}
