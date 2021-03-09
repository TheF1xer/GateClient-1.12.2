package com.thef1xer.gateclient.modules;

public enum EnumModuleCategory {
    //TODO: Color rework?
    COMBAT("Combat", 0xE54343),
    HUD("HUD", 0xDC33E4),
    MOVEMENT("Movement", 0x6399FF),
    PLAYER("Player", 0xFF8031),
    RENDER("Render", 0xFFDF29);

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