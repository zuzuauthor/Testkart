
package com.testkart.exam.edu.leaderboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Response implements Serializable
{

    @SerializedName("Selection")
    @Expose
    private Selection selection;
    private final static long serialVersionUID = -1864520961737266852L;

    public Selection getSelection() {
        return selection;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }

}
