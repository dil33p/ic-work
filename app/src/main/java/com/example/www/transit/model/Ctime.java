package com.example.www.transit.model;

/**
 * Created by jaskaran on 23/7/16.
 */
public class Ctime {
    public String text;
    public String timeZone;
    public long value;

    public Ctime(String text, String timeZone, long value) {
        this.text = text;
        this.timeZone = timeZone;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
