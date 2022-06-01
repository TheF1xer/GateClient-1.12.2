package me.thef1xer.gateclient.settings.impl;

import me.thef1xer.gateclient.settings.Setting;
import net.minecraft.util.text.TextFormatting;

public class RGBSetting extends Setting {
    private int red;
    private int green;
    private int blue;

    public RGBSetting(String name, String id, int red, int green, int blue) {
        super(name, id);
        this.red = red;
        this.green = green;
        this.blue = blue;
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

    @Override
    public String getCommandSyntax() {
        return TextFormatting.GOLD.toString() + TextFormatting.ITALIC + getName() + ": " +
                TextFormatting.RESET + getId() + " <red> <green> <blue> " +
                TextFormatting.GOLD + TextFormatting.ITALIC + "[" + getRed() + ", " + getGreen() + ", " + getBlue() + "]";
    }
}
