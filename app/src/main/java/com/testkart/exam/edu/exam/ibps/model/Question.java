
package com.testkart.exam.edu.exam.ibps.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("qtype_id")
    @Expose
    private String qtypeId;
    @SerializedName("subject_id")
    @Expose
    private String subjectId;
    @SerializedName("topic_id")
    @Expose
    private String topicId;
    @SerializedName("stopic_id")
    @Expose
    private String stopicId;
    @SerializedName("diff_id")
    @Expose
    private String diffId;
    @SerializedName("passage_id")
    @Expose
    private String passageId;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("option1")
    @Expose
    private String option1;
    @SerializedName("option2")
    @Expose
    private String option2;
    @SerializedName("option3")
    @Expose
    private String option3;
    @SerializedName("option4")
    @Expose
    private String option4;
    @SerializedName("option5")
    @Expose
    private String option5;
    @SerializedName("option6")
    @Expose
    private String option6;
    @SerializedName("marks")
    @Expose
    private String marks;
    @SerializedName("negative_marks")
    @Expose
    private String negativeMarks;
    @SerializedName("hint")
    @Expose
    private String hint;
    @SerializedName("explanation")
    @Expose
    private String explanation;
    @SerializedName("answer")
    @Expose
    private String answer;
    @SerializedName("true_false")
    @Expose
    private Object trueFalse;
    @SerializedName("fill_blank")
    @Expose
    private String fillBlank;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("Subject")
    @Expose
    private Subject subject;
    @SerializedName("Qtype")
    @Expose
    private Qtype qtype;
    @SerializedName("Passage")
    @Expose
    private Passage passage;
    @SerializedName("QuestionsLang")
    @Expose
    private List<OtherLangQuestion> questionsLang = null;
    private final static long serialVersionUID = 7402129887576900852L;

    public List<OtherLangQuestion> getQuestionsLang() {
        return questionsLang;
    }

    public void setQuestionsLang(List<OtherLangQuestion> questionsLang) {
        this.questionsLang = questionsLang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQtypeId() {
        return qtypeId;
    }

    public void setQtypeId(String qtypeId) {
        this.qtypeId = qtypeId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getStopicId() {
        return stopicId;
    }

    public void setStopicId(String stopicId) {
        this.stopicId = stopicId;
    }

    public String getDiffId() {
        return diffId;
    }

    public void setDiffId(String diffId) {
        this.diffId = diffId;
    }

    public String getPassageId() {
        return passageId;
    }

    public void setPassageId(String passageId) {
        this.passageId = passageId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getOption5() {
        return option5;
    }

    public void setOption5(String option5) {
        this.option5 = option5;
    }

    public String getOption6() {
        return option6;
    }

    public void setOption6(String option6) {
        this.option6 = option6;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getNegativeMarks() {
        return negativeMarks;
    }

    public void setNegativeMarks(String negativeMarks) {
        this.negativeMarks = negativeMarks;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Object getTrueFalse() {
        return trueFalse;
    }

    public void setTrueFalse(Object trueFalse) {
        this.trueFalse = trueFalse;
    }

    public String getFillBlank() {
        return fillBlank;
    }

    public void setFillBlank(String fillBlank) {
        this.fillBlank = fillBlank;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Qtype getQtype() {
        return qtype;
    }

    public void setQtype(Qtype qtype) {
        this.qtype = qtype;
    }

    public Passage getPassage() {
        return passage;
    }

    public void setPassage(Passage passage) {
        this.passage = passage;
    }



}
