package com.thef1xer.gateclient.settings;

public class FloatSetting extends Setting{
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
        this(name, id, value, 0F, 100F);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }
}
