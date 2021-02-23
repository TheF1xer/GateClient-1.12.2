package com.thef1xer.gateclient.settings.impl;

import com.thef1xer.gateclient.settings.Setting;

public class EnumSetting<T extends Enum<?>> extends Setting {
    private final T[] values;
    private T currentValue;

    public EnumSetting(String name, String id, T[] values, T currentValue) {
        super(name, id);
        this.values = values;
        this.currentValue = currentValue;
    }

    public T[] getValues() {
        return values;
    }

    public T getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(T currentValue) {
        this.currentValue = currentValue;
    }

    public boolean setValueFromName(String name) {
        for (T value : values) {
            if (name.equalsIgnoreCase(value.toString())) {
                this.setCurrentValue(value);
                return true;
            }
        }
        return false;
    }

    public String getCurrentValueName() {
        return currentValue.toString();
    }
}
