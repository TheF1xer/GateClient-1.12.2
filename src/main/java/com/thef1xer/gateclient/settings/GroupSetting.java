package com.thef1xer.gateclient.settings;

public abstract class GroupSetting extends Setting {

    public GroupSetting(String name) {
        super(name, null);
    }

    public abstract Setting[] getSettings();
}