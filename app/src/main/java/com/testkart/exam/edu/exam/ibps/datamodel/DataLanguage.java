package com.testkart.exam.edu.exam.ibps.datamodel;

import java.io.Serializable;

/**
 * Created by testkart on 1/7/17.
 */

public class DataLanguage implements Serializable {

    private String langId;
    private String langName;

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

    public String getLangName() {
        return langName;
    }

    public void setLangName(String langName) {
        this.langName = langName;
    }
}
