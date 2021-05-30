package com.thef1xer.gateclient.settings.impl;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.settings.Setting;

public class EnumSetting extends Setting {
    private final Enum<?>[] values;
    private Enum<?> currentValue;

    public EnumSetting(String name, String id, Enum<?>[] values, Enum<?> currentValue) {
        super(name, id);
        this.values = values;
        this.currentValue = currentValue;
    }

    public Enum<?>[] getValues() {
        return values;
    }

    public Enum<?> getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Enum<?> currentValue) {
        this.currentValue = currentValue;
    }

    public boolean setValueFromName(String name) {
        for (Enum<?> value : values) {
            if (name.equalsIgnoreCase(value.toString())) {
                setCurrentValue(value);
                return true;
            }
        }
        return false;
    }

    public String getCurrentValueName() {
        return currentValue.toString();
    }
}
