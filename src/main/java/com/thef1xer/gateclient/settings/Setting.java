package com.thef1xer.gateclient.settings;

public abstract class Setting {
    public String name;
    public String id;

    public Setting(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
