package com.thef1xer.gateclient.settings;

public class EnumSetting<T extends Enum<?>> extends Setting{
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
            if (value instanceof IEnumSetting) {
                if (((IEnumSetting) value).getName().equalsIgnoreCase(name)) {
                    this.setCurrentValue(value);
                    return true;
                }
            }
        }
        return false;
    }

    public String getCurrentValueName() {
        if (this.getCurrentValue() instanceof IEnumSetting) {
            return ((IEnumSetting) this.getCurrentValue()).getName();
        }
        return null;
    }

    public interface IEnumSetting {
        public String getName();
    }
}
