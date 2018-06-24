
package com.testkart.exam.edu.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SysSetting implements Serializable
{

    @SerializedName("Configuration")
    @Expose
    private Configuration configuration;
    private final static long serialVersionUID = -1691758610171114249L;

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

}
