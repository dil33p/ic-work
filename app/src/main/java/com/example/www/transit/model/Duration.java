package com.example.www.transit.model;

import java.io.Serializable;

/**
 * Created by jaskaran on 20/7/16.
 */
public class Duration implements Serializable{
    public Duration(String text, long value) {
        this.text = text;
        this.value = value;
    }

    private String text;
    private long value;

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
