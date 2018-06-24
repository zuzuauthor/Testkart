package com.testkart.exam.edu.mailbox;

/**
 * Created by testkart on 24/5/17.
 */

public class MyCurrency {

    private String symbol;
    private String desc;

    public MyCurrency(String symbol, String desc) {
        this.symbol = symbol;
        this.desc = desc;
    }


    public String getSymbol() {
        return this.symbol;
    }

    public String getDesc() {
        return this.desc;
    }
}
