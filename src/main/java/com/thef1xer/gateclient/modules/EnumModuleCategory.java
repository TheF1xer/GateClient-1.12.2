package com.thef1xer.gateclient.modules;

public enum EnumModuleCategory {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    RENDER("Render");


    private String name;
    EnumModuleCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}