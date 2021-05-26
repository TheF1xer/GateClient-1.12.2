package com.thef1xer.gateclient.settings.impl;

import com.thef1xer.gateclient.settings.Setting;

public class FloatSetting extends Setting {
    private float value;
    private final float min;
    private final float max;

    public FloatSetting(String name, String id, float value, float min, float max) {
        super(name, id);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public FloatSetting(String name, String id, float value) {
        this(name, id, value, 0F, 10F);
    }

    public float getValue() {
        return value;
    }

    public boolean setValue(float value) {
        if (value >= min && value <= max) {
            this.value = value;
            return true;
        }
        return false;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }
}
