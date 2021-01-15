package com.thef1xer.gateclient.settings;

public abstract class GroupSetting extends Setting {

    public GroupSetting(String name, String id) {
        super(name, id);
    }

    public Setting[] getSettings() {
        return null;
    }
}