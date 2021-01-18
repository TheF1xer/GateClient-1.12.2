package com.thef1xer.gateclient.modules;

import com.thef1xer.gateclient.settings.Setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module {
    private final String name;
    private final String id;
    private boolean enabled = false;
    private int keyBind;
    private final EnumModuleCategory moduleCategory;
    private boolean drawOnHud = true;
    private final List<Setting> settings = new ArrayList<>();

    public Module(String name, String id, EnumModuleCategory category, int key) {
        this.name = name;
        this.id = id;
        this.moduleCategory = category;
        this.keyBind = key;
    }

    public void onEnabled() {

    }

    public void onDisabled() {

    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return id;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean set) {
        if (set) {
            this.enabled = true;
            this.onEnabled();
        } else {
            this.enabled = false;
            this.onDisabled();
        }
    }

    public void toggle() {
        setEnabled(!this.enabled);
    }

    public int getKeyBind() {
        return this.keyBind;
    }

    public void setKeyBind(int key) {
        this.keyBind = key;
    }

    public EnumModuleCategory getModuleCategory() {
        return this.moduleCategory;
    }

    public boolean getDrawOnHud() {
        return this.drawOnHud;
    }

    public void setDrawOnHud(boolean draw) {
        this.drawOnHud = draw;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public void addSettings(Setting... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }
}
