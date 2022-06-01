package me.thef1xer.gateclient.settings.impl;

import me.thef1xer.gateclient.settings.Setting;
import net.minecraft.util.text.TextFormatting;

public class BooleanSetting extends Setting {
    private boolean value;

    public BooleanSetting(String name, String id, boolean value) {
        super(name, id);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void toggle() {
        this.setValue(!this.value);
    }

    @Override
    public String getCommandSyntax() {
        return TextFormatting.GOLD.toString() + TextFormatting.ITALIC + getName() + ": " +
                TextFormatting.RESET + getId() + " <true / false> " + TextFormatting.GOLD + TextFormatting.ITALIC + "[" + getValue() + "]";
    }
}
