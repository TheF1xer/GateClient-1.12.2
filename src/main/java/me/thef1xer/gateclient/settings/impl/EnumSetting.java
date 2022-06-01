package me.thef1xer.gateclient.settings.impl;

import me.thef1xer.gateclient.settings.Setting;
import net.minecraft.util.text.TextFormatting;

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

    @Override
    public String getCommandSyntax() {
        String valuesToString = " <";

        for (int i = 0; i < values.length; i++) {
            valuesToString = valuesToString.concat(values[i].toString());

            if (i != values.length - 1) {
                valuesToString = valuesToString.concat(" / ");
            }
        }

        valuesToString = valuesToString.concat("> ");

        return TextFormatting.GOLD.toString() + TextFormatting.ITALIC + getName() + ": " +
                TextFormatting.RESET + getId() + valuesToString + TextFormatting.GOLD + TextFormatting.ITALIC + "[" + getCurrentValueName() + "]";
    }
}
