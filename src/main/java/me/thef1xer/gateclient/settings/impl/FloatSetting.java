package me.thef1xer.gateclient.settings.impl;

import me.thef1xer.gateclient.settings.Setting;

public class FloatSetting extends Setting {
    private float value;
    private final float min;
    private final float max;
    private final float step;

    public FloatSetting(String name, String id, float value, float min, float max, float step) {
        super(name, id);
        this.value = value;
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public FloatSetting(String name, String id, float value, float min, float max) {
        this(name, id, value, min, max, 0.1F);
    }

    public FloatSetting(String name, String id, float value) {
        this(name, id, value, 0F, 10F, 0.1F);
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

    public boolean setValueWithStep(float value) {
        return setValue(this.step * Math.round(value / this.step));
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float getStep() {
        return step;
    }
}
