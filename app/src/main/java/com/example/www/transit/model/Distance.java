package com.example.www.transit.model;

/**
 * Created by jaskaran on 20/7/16.
 */
public class Distance {
    private String text;
    private long value;

    public Distance(String text, long value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

}
