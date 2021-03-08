package com.thef1xer.gateclient.modules;

public enum EnumModuleCategory {
    COMBAT("Combat", 0xE35624),
    HUD("HUD", 0xA900B3),
    MOVEMENT("Movement", 0x009AE5),
    PLAYER("Player", 0xFFA600),
    RENDER("Render", 0x0DC63D);


    private final String name;
    private final int color;
    EnumModuleCategory(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }
}