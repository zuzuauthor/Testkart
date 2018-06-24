
package com.testkart.exam.edu.help;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Response implements Serializable
{

    @SerializedName("Help")
    @Expose
    private Help help;
    private final static long serialVersionUID = 4114943008381078819L;

    public Help getHelp() {
        return help;
    }

    public void setHelp(Help help) {
        this.help = help;
    }

}
