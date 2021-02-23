package com.thef1xer.gateclient.settings.impl;

import com.thef1xer.gateclient.settings.Setting;

public class ColorSetting extends Setting {
    private int red;
    private int green;
    private int blue;
    private int alpha;

    public ColorSetting(String name, String id, int red, int green, int blue, int alpha) {
        super(name, id);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public ColorSetting(String name, String id, int red, int green, int blue) {
        this(name, id, red, green, blue, 255);
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }
}
