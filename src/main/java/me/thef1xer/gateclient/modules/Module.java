package me.thef1xer.gateclient.modules;

import me.thef1xer.gateclient.settings.Setting;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module {
    private final String name;
    private final String id;
    private boolean enabled = false;
    private int keyBind;
    private final ModuleCategory moduleCategory;
    private final List<Setting> settings = new ArrayList<>();

    public final BooleanSetting drawOnHud = new BooleanSetting("Draw on Hud", "drawonhud", true);

    public Module(String name, String id, ModuleCategory category) {
        this.name = name;
        this.id = id;
        this.moduleCategory = category;
        this.keyBind = Keyboard.KEY_NONE;
        this.addSettings(drawOnHud);
    }

    public Module(String name, String id, int keyBind, ModuleCategory category) {
        this.name = name;
        this.id = id;
        this.keyBind = keyBind;
        this.moduleCategory = category;
        this.keyBind = Keyboard.KEY_NONE;
        this.addSettings(drawOnHud);
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

    public void addSettings(Setting... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }

    public int getKeyBind() {
        return this.keyBind;
    }

    public void setKeyBind(int key) {
        this.keyBind = key;
    }

    public ModuleCategory getModuleCategory() {
        return this.moduleCategory;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public enum ModuleCategory {
        COMBAT("Combat", 0xE54343),
        HUD("HUD", 0xDC33E4),
        MOVEMENT("Movement", 0x6399FF),
        PLAYER("Player", 0xFF8031),
        RENDER("Render", 0xFFDF29);

        private final String name;
        private final int color;
        ModuleCategory(String name, int color) {
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
}
