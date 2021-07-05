package com.thef1xer.gateclient.modules.hud;

import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.impl.RGBSetting;
import com.thef1xer.gateclient.settings.impl.EnumSetting;

public class ModuleList extends Module {
    public static final ModuleList INSTANCE = new ModuleList();

    public final EnumSetting color = new EnumSetting("List Color", "color", Color.values(), Color.STATIC);
    public final RGBSetting staticColor = new RGBSetting("Static Color", "static", 255, 255, 255);

    public ModuleList() {
        super("Module List", "modulelist", Module.ModuleCategory.HUD);
        this.addSettings(color, staticColor);
    }

    public enum Color {
        STATIC("Static"),
        CATEGORY("Category"),
        RAINBOW("Rainbow");

        private final String name;
        Color(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
